package com.lim.assemble.todayassemble.events.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.AccountsEventsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import com.lim.assemble.todayassemble.accounts.repository.AccountsEventsRepository;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.ValidateSituationType;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.entity.EventsImages;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.events.service.EventsService;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.tags.entity.Tags;
import com.lim.assemble.todayassemble.validation.ValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final ValidationFactory validationFactory;
    private final EventsRepository eventsRepository;
    private final AccountsRepository accountsRepository;
    private final AccountsEventsRepository accountsEventsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventsDto> getEventsList(Pageable pageable) {
        return eventsRepository.getEventsList(pageable, LocalDateTime.now()).stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getEventsListSize() {

        return Optional.of(eventsRepository.findByEventsTimeGreaterThan(LocalDateTime.now()))
                .orElse(new ArrayList<Events>())
                .size();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsDto> searchEventsList(String keyword, Pageable pageable) {
        return eventsRepository.findByKeyword(pageable, keyword, LocalDateTime.now()).stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer searchEventsSize(String keyword) {
        return eventsRepository.findByKeywordSize(keyword, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventsDto> searchEventsListByPlace(String keyword, Pageable pageable) {
        return eventsRepository.findByPlace(pageable, keyword, LocalDateTime.now()).stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer searchEventsSizeByPlace(String keyword) {
        return eventsRepository.findByPlaceSize(keyword, LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public EventsDto getEvents(Long eventId) {

        return EventsDto.from(findEventsById(eventId));
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getParticipateEventsAccounts(Long eventId) {
        return accountsEventsRepository.findByEventsId(eventId)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID))
                .size();
    }

    private Events findEventsById(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID));
    }

    @Override
    @Transactional
    public EventsDto createEvents(CreateEventsReq createEventsReq, Accounts accounts) {

        // createEventsReq validation check : {
        // ???????????? ??????????????? ????????? ????????? ????????? ?????? ????????? ????????? ???????????? ??????
        // , eventsType ??? ?????? ??? ??????
        // }
        Events events = Events.of(createEventsReq, accounts);
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.CREATE, events);

        // events ??????
        events = eventsRepository.save(events);
        accountsMappingEvents(accounts, events);

        return EventsDto.from(
                events
        );
    }

    public void accountsMappingEvents(Accounts accounts, Events events) {
        Optional<AccountsMapperEvents> accountsMapperEventsOptional
                = accountsEventsRepository.findByAccountsIdAndEventsId(accounts.getId(), events.getId());

        // 1. ???????????? ????????? ?????? -> ?????????
        if (accountsMapperEventsOptional.isPresent()) {
            leaveEvents(accountsMapperEventsOptional.get(), accounts, events);
        } else {
            // 2. ???????????? ????????? ?????? ?????? -> ??????
            AccountsMapperEvents accountsMapperEvents = AccountsMapperEvents.builder()
                    .accounts(accounts)
                    .events(events)
                    .isParticipating(true)
                    .build();
            createAccountMapperEvents(accounts, events, accountsMapperEvents);
        }

    }

    public void createAccountMapperEvents(Accounts accounts, Events events, AccountsMapperEvents accountsMapperEvents) {
        // events validation check: {maxNum ??????}
        validationFactory.createValidation(ValidateType.EVENT).validate(
                ValidateSituationType.EVENTS_PARTICIPATE
                , events.getId()
        );

        addAccountsMapperEventsToSet(accountsMapperEvents, accounts.getAccountsEventsSet());
        addAccountsMapperEventsToSet(accountsMapperEvents, events.getAccountsEventsSet());
    }

    public void leaveEvents(AccountsMapperEvents accountsMapperEvents, Accounts accounts, Events events) {

        // ????????? mapperEntity ??????
        deleteAccountsMapperEventsSet(accountsMapperEvents, accounts.getAccountsEventsSet());
        deleteAccountsMapperEventsSet(accountsMapperEvents, events.getAccountsEventsSet());

        // ???????????? ??????, ?????? events ??????
        if (accounts.getId().equals(events.getAccounts().getId())) {
            eventsRepository.delete(events);
        }
    }

    public void deleteAccountsMapperEventsSet(
            AccountsMapperEvents accountsMapperEvents
            , Set<AccountsMapperEvents> accountsEventsSet
    ) {
        AccountsMapperEvents delete = accountsEventsSet.stream()
                                        .filter(item -> item.getId().equals(accountsMapperEvents.getId()))
                                        .findFirst()
                                        .orElseThrow(
                                                () -> new TodayAssembleException(ErrorCode.NO_ACCOUNTS_MAPPER_EVENTS_ID)
                                        );
        accountsEventsSet.remove(delete);
    }

    public void addAccountsMapperEventsToSet(
            AccountsMapperEvents accountsMapperEvents
            , Set<AccountsMapperEvents> accountsMapperEventsSet
    ) {
        if (accountsMapperEventsSet == null) {
            accountsMapperEventsSet = new HashSet<>();
        }
        accountsMapperEventsSet.add(accountsMapperEvents);
    }

    @Override
    @Transactional
    public EventsDto updateEvents(UpdateEventsReqBase updateEventsReqBase, Accounts accounts) {
        Events events = eventsRepository.getById(updateEventsReqBase.getId());
        EventsDto eventsDto = EventsDto.builder().build();
        updateEventsReqBase.setAccountsId(accounts.getId());
        // updateEventsReqBase ??? ???????????? ????????? ?????? ??????
        if (UpdateEventsContentsReq.class.equals(updateEventsReqBase.getClass())) {
            eventsDto = updateEventsContents(events, (UpdateEventsContentsReq) updateEventsReqBase);
        } else if (UpdateEventsImagesReq.class.equals(updateEventsReqBase.getClass())) {
            eventsDto = updateEventsImages(events, (UpdateEventsImagesReq) updateEventsReqBase);
        }

        return eventsDto;
    }

    public EventsDto updateEventsContents(Events events, UpdateEventsContentsReq updateEventsContentsReq) {
        // updateEventsReq validation check : { ???????????? ??????????????? ????????? ????????? ????????? ?????? ????????? ????????? ???????????? ??????, ???????????? ????????? ??????}
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.UPDATE, updateEventsContentsReq);

        // events ??????
        events.update(updateEventsContentsReq);

        // events tag ??????
        updateEventsTags(events, updateEventsContentsReq.getTags());

        // events image ??????
        updateEventsImages(events, updateEventsContentsReq);

        return EventsDto.from(events);
    }

    public void updateEventsTags(Events events, Set<String> tags) {

        Set<Tags> tagsSet = events.getTagsSet();

        // ??? tag ?????? ????????? <= ?????? ?????? ??????.
        if (tags == null || tags.size() <= 0) {
            // ?????? tag ????????? ????????? ??????.
            if (tagsSet == null) {
                return;
            }
            // ??? tag ??? ?????? ??? ??????.
            tags = new HashSet<>();
        }

        // events tag ??????
        // ?????? ????????? ?????? tags ?????????.
        if (tagsSet != null) {
            tagsSet.clear();
        } else {
            events.setTagsSet(new HashSet<>());
        }

        // ?????? ?????? ???????????? ????????? tags ????????????.
        events.getTagsSet().addAll(
                tags.stream()
                        .map(tag -> Tags.of(tag, events)) // Tags Entity??? ???????????? ??? Tags?????? events ??????.
                        .collect(Collectors.toSet())
        );
    }

    public EventsDto updateEventsImages(Events events, UpdateEventsImagesReq updateEventsImagesReq) {
        // updateEventsImagesReq validation check : {???????????? ????????? ??????}
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.UPDATE_IMAGES, updateEventsImagesReq);

        if (events.getEventsImagesSet() != null) {
            events.getEventsImagesSet().clear();
        } else {
            events.setEventsImagesSet(new HashSet<>());
        }

        Set<EventsImagesDto> eventsImagesDtos = new HashSet<>();
        if (updateEventsImagesReq.getImages() != null && updateEventsImagesReq.getImages().size() > 0) {
            eventsImagesDtos = updateEventsImagesReq.getImages();
        }

        events.getEventsImagesSet().addAll(
                eventsImagesDtos.stream()
                    .map(images -> EventsImages.of(images, events))
                    .collect(Collectors.toSet())
        );

        return EventsDto.from(events);
    }

    @Override
    @Transactional
    public void deleteEvents(Long eventsId, Accounts accounts) {
        // deleteEventsDto validation check : {???????????? ????????? ??????}
        DeleteEventsDto deleteEventsDto = new DeleteEventsDto();
        deleteEventsDto.setId(eventsId);
        deleteEventsDto.setAccountsId(accounts.getId());
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.DELETE, deleteEventsDto);

        // event ??????
        eventsRepository.deleteById(eventsId);
    }

    @Override
    @Transactional
    public AccountsEventsDto<EventsDto> participateEventsManage(Long eventsId, Accounts accounts) {

        Events events = findEventsById(eventsId);
        accountsMappingEvents(accounts, events);
        AccountsEventsDto<EventsDto> accountsEventsDto = AccountsEventsDto.from(events);
        return accountsEventsDto;
    }

    @Override
    @Transactional
    public AccountsEventsDto<EventsDto> inviteEvents(Long eventsId, Accounts accounts, Long inviteAccountsId) {
        // inviteEvents validation check : {???????????? ????????? ??????, ?????? ?????? ??????????????? ??????, ?????? ????????? ?????? ????????? ?????? ,?????? ????????? ???????????? ?????? ????????? ??????}
        validationFactory.createValidation(ValidateType.EVENT).validate(
                ValidateSituationType.INVITE
                , eventsId, accounts, inviteAccountsId
        );

        Events events = eventsRepository.getById(eventsId);
        Accounts inviteAccounts = accountsRepository.getById(inviteAccountsId);
        AccountsMapperEvents accountsMapperEvents = AccountsMapperEvents.builder()
                .accounts(inviteAccounts)
                .events(events)
                .isParticipating(false)
                .build();

        createAccountMapperEvents(inviteAccounts, events, accountsMapperEvents);
        AccountsEventsDto<EventsDto> eventsDto = AccountsEventsDto.from(events);
        return eventsDto;
    }

    @Override
    @Transactional
    public AccountsEventsDto<AccountsDto> responseEventsInvite(
            Long eventsId
            , Accounts accounts
            , UpdateAccountsMapperEventsReq updateAccountsMapperEventsReq) {
        // responseInviteEvents validation check : {?????? eventsId??? ?????? ?????? ?????? ??? ??????}
        validationFactory.createValidation(ValidateType.EVENT).validate(
                ValidateSituationType.RESPONSE_EVENTS_INVITE
                , eventsId, accounts.getId());

        Events events = eventsRepository.getById(eventsId);
        AccountsMapperEvents accountsMapperEvents = accountsEventsRepository
                .findByAccountsIdAndEventsId(accounts.getId(), eventsId).get();

        if (updateAccountsMapperEventsReq.getResponse()) {
            accountsMapperEvents.setIsParticipating(true);
            createAccountMapperEvents(accounts, events, accountsMapperEvents);
        } else {
            leaveEvents(accountsMapperEvents, accounts, events);
        }

        AccountsEventsDto<AccountsDto> accountsEventsDto = AccountsEventsDto.from(accounts);
        return accountsEventsDto;
    }


}
