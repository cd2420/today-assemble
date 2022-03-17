package com.lim.assemble.todayassemble.events.service.impl;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.accounts.dto.AccountsEventsDto;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import com.lim.assemble.todayassemble.accounts.repository.AccountsEventsRepository;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.type.EventsType;
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
import com.lim.assemble.todayassemble.zooms.entity.Zooms;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        return eventsRepository.findAll(pageable).stream()
                .map(EventsDto::from)
                .collect(Collectors.toList());

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
        // 호스트가 생성할려는 모임이 기존에 자기가 만든 모임과 시간이 겹치는지 체크
        // , eventsType 별 필수 값 체크
        // }
        Events events = Events.of(createEventsReq, accounts);
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.CREATE, events);

        accounts = accountsRepository.findById(accounts.getId()).get();

        // events 생성
        events = eventsRepository.save(events);
        accountsMappingEvents(accounts, events);

        return EventsDto.from(
                events
        );
    }

    public void accountsMappingEvents(Accounts accounts, Events events) {
        Optional<AccountsMapperEvents> accountsMapperEventsOptional
                = accountsEventsRepository.findByAccountsIdAndEventsId(accounts.getId(), events.getId());

        // 1. 참여중인 모임일 경우 -> 떠나기
        if (accountsMapperEventsOptional.isPresent()) {
            leaveEvents(accountsMapperEventsOptional.get(), accounts, events);
        } else {
            // 2. 참여중인 모임이 아닌 경우 -> 참여
            AccountsMapperEvents accountsMapperEvents = AccountsMapperEvents.builder()
                    .accounts(accounts)
                    .events(events)
                    .accept(true)
                    .build();
            createAccountMapperEvents(accounts, events, accountsMapperEvents);
        }

    }

    public void createAccountMapperEvents(Accounts accounts, Events events, AccountsMapperEvents accountsMapperEvents) {
        // events validation check: {maxNum 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(
                ValidateSituationType.EVENTS_PARTICIPATE
                , events.getId()
        );

        addAccountsMapperEventsToSet(accountsMapperEvents, accounts.getAccountsEventsSet());
        addAccountsMapperEventsToSet(accountsMapperEvents, events.getAccountsEventsSet());
    }

    public void leaveEvents(AccountsMapperEvents accountsMapperEvents, Accounts accounts, Events events) {

        // 자식인 mapperEntity 제거
        deleteAccountsMapperEventsSet(accountsMapperEvents, accounts.getAccountsEventsSet());
        deleteAccountsMapperEventsSet(accountsMapperEvents, events.getAccountsEventsSet());

        // 호스트인 경우, 해당 events 삭제
        if (accounts.getId().equals(events.getHostAccountsId())) {
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
        EventsDto eventsDto = EventsDto.builder().build();
        updateEventsReqBase.setAccountsId(accounts.getId());
        // updateEventsReqBase 를 구체화한 객체에 따라 분기
        if (UpdateEventsContentsReq.class.equals(updateEventsReqBase.getClass())) {
            eventsDto = updateEventsContents((UpdateEventsContentsReq) updateEventsReqBase);
        } else if (UpdateEventsTagsReq.class.equals(updateEventsReqBase.getClass())) {
            eventsDto = updateEventsTags((UpdateEventsTagsReq) updateEventsReqBase);
        } else if (UpdateEventsImagesReq.class.equals(updateEventsReqBase.getClass())) {
            eventsDto = updateEventsImages((UpdateEventsImagesReq) updateEventsReqBase);
        } else if (UpdateEventsTypeReq.class.equals(updateEventsReqBase.getClass())) {
            eventsDto = updateEventsType((UpdateEventsTypeReq) updateEventsReqBase);
        }

        return eventsDto;
    }

    public EventsDto updateEventsContents(UpdateEventsContentsReq updateEventsContentsReq) {
        // updateEventsReq validation check : { 호스트가 수정할려는 모임이 기존에 자기가 만든 모임과 시간이 겹치는지 체크, 호스트가 맞는지 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.UPDATE, updateEventsContentsReq);

        // events 수정
        Events events = eventsRepository.getById(updateEventsContentsReq.getId());
        events.update(updateEventsContentsReq);

        return EventsDto.from(events);
    }

    public EventsDto updateEventsTags(UpdateEventsTagsReq updateEventsTagsReq) {

        // updateEventsTagsReq validation check : {호스트가 맞는지 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.UPDATE_TAGS, updateEventsTagsReq);

        // events tag 수정
        Events events = eventsRepository.getById(updateEventsTagsReq.getId());
        if (events.getTagsSet() != null) {
            events.getTagsSet().clear();
        } else {
            events.setTagsSet(new HashSet<>());
        }
        events.getTagsSet().addAll(
                updateEventsTagsReq.getTags().stream()
                    .map(tags -> Tags.of(tags, events))
                    .collect(Collectors.toSet())
        );

        return EventsDto.from(events);
    }

    public EventsDto updateEventsImages(UpdateEventsImagesReq updateEventsImagesReq) {
        // updateEventsImagesReq validation check : {호스트가 맞는지 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.UPDATE_IMAGES, updateEventsImagesReq);

        // events Images 수정
        Events events = eventsRepository.getById(updateEventsImagesReq.getId());

        if (events.getEventsImagesSet() != null) {
            events.getEventsImagesSet().clear();
        } else {
            events.setEventsImagesSet(new HashSet<>());
        }

        events.getEventsImagesSet().addAll(
                updateEventsImagesReq.getImages().stream()
                    .map(images -> EventsImages.of(images, events))
                    .collect(Collectors.toSet())
        );

        return EventsDto.from(events);
    }

    public EventsDto updateEventsType(UpdateEventsTypeReq updateEventsTypeReq) {
        // updateEventsTypeReq validation check : {호스트가 맞는지 체크, Type 별 주소 값 또는 줌 값 필수 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.UPDATE_EVENTS_TYPE, updateEventsTypeReq);

        // 1.Type 별 코드 수정
        Events events = eventsRepository.getById(updateEventsTypeReq.getId());
        events.getZoomsSet().clear();
        events.setAddress("");
        events.setLongitude("");
        events.setLatitude("");
        events.setEventsType(updateEventsTypeReq.getEventsType());
        // 1-1. Type 이 Offline -> Online 되는 경우
        if (EventsType.ONLINE.equals(updateEventsTypeReq.getEventsType())) {
            events.getZoomsSet().addAll(
                    updateEventsTypeReq.getZooms().stream()
                        .map(zoomsDto -> Zooms.of(zoomsDto, events))
                        .collect(Collectors.toSet())
            );
        } else {
            // 1-2. Type 이 Online -> Offline 되는 경우
            events.setAddress(updateEventsTypeReq.getAddress());
            events.setLongitude(updateEventsTypeReq.getLongitude());
            events.setLatitude(updateEventsTypeReq.getLatitude());
        }

        return EventsDto.from(events);

    }


    @Override
    @Transactional
    public void deleteEvents(Long eventsId, Accounts accounts) {
        // deleteEventsDto validation check : {호스트가 맞는지 체크}
        DeleteEventsDto deleteEventsDto = new DeleteEventsDto();
        deleteEventsDto.setId(eventsId);
        deleteEventsDto.setAccountsId(accounts.getId());
        validationFactory.createValidation(ValidateType.EVENT).validate(ValidateSituationType.DELETE, deleteEventsDto);

        // event 삭제
        eventsRepository.deleteById(eventsId);
    }

    @Override
    @Transactional
    public AccountsEventsDto<AccountsDto> participateEventsManage(Long eventsId, Accounts accounts) {

        Events events = findEventsById(eventsId);
        accounts = accountsRepository.getById(accounts.getId());
        accountsMappingEvents(accounts, events);
        AccountsEventsDto<AccountsDto> accountsEventsDto = AccountsEventsDto.from(accounts);
        return accountsEventsDto;
    }

    @Override
    @Transactional
    public AccountsEventsDto<EventsDto> inviteEvents(Long eventsId, Accounts accounts, Long inviteAccountsId) {
        // inviteEvents validation check : {호스트가 맞는지 체크, 최대 인원 초과하는지 체크, 초대 계정이 존재 하는지 체크 ,초대 할려는 사용자가 이미 있는지 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(
                ValidateSituationType.INVITE
                , eventsId, accounts, inviteAccountsId
        );

        Events events = eventsRepository.getById(eventsId);
        Accounts inviteAccounts = accountsRepository.getById(inviteAccountsId);
        AccountsMapperEvents accountsMapperEvents = AccountsMapperEvents.builder()
                .accounts(inviteAccounts)
                .events(events)
                .accept(false)
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
        // responseInviteEvents validation check : {해당 eventsId에 초대 된게 맞는 지 체크}
        validationFactory.createValidation(ValidateType.EVENT).validate(
                ValidateSituationType.RESPONSE_EVENTS_INVITE
                , eventsId, accounts.getId());

        accounts = accountsRepository.getById(accounts.getId());
        Events events = eventsRepository.getById(eventsId);
        AccountsMapperEvents accountsMapperEvents = accountsEventsRepository
                .findByAccountsIdAndEventsId(accounts.getId(), eventsId).get();

        if (updateAccountsMapperEventsReq.getResponse()) {
            accountsMapperEvents.setAccept(true);
            createAccountMapperEvents(accounts, events, accountsMapperEvents);
        } else {
            leaveEvents(accountsMapperEvents, accounts, events);
        }

        AccountsEventsDto<AccountsDto> accountsEventsDto = AccountsEventsDto.from(accounts);
        return accountsEventsDto;
    }
}
