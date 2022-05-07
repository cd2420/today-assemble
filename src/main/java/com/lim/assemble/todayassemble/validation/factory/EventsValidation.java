package com.lim.assemble.todayassemble.validation.factory;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.entity.AccountsMapperEvents;
import com.lim.assemble.todayassemble.accounts.repository.AccountsEventsRepository;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.common.entity.Images;
import com.lim.assemble.todayassemble.common.type.EventsType;
import com.lim.assemble.todayassemble.common.type.ImagesType;
import com.lim.assemble.todayassemble.common.type.ValidateSituationType;
import com.lim.assemble.todayassemble.common.type.ValidateType;
import com.lim.assemble.todayassemble.events.dto.*;
import com.lim.assemble.todayassemble.events.entity.Events;
import com.lim.assemble.todayassemble.events.entity.EventsImages;
import com.lim.assemble.todayassemble.events.repository.EventsRepository;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import com.lim.assemble.todayassemble.zooms.dto.ZoomsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventsValidation implements Validation {

    private final EventsRepository eventsRepository;
    private final AccountsRepository accountsRepository;
    private final AccountsEventsRepository accountsEventsRepository;

    private final ValidateType validateType = ValidateType.EVENT;

    @Override
    public ValidateType getValidateType() {
        return validateType;
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(ValidateSituationType validateSituationType, Object... target) {

        if (ValidateSituationType.CREATE.equals(validateSituationType)) {
            // 생성 validate
            createValidate((Events) target[0]);
        } else if (ValidateSituationType.UPDATE.equals(validateSituationType)) {
            // 수정 validate
            updateValidate((UpdateEventsContentsReq) target[0]);
        } else if(ValidateSituationType.UPDATE_IMAGES.equals(validateSituationType)) {
            // 이미지 수정시 validate
            updateEventsImagesValidate((UpdateEventsImagesReq) target[0]);
        } else if(ValidateSituationType.DELETE.equals(validateSituationType)) {
            //  event 삭제시 validate
            deleteEventsValidate((UpdateEventsReqBase) target[0]);
        } else if(ValidateSituationType.EVENTS_PARTICIPATE.equals(validateSituationType)) {
            // 모임 참여할 때 참여가능한지 validate
            participateValidate((Long) target[0]);
        } else if (ValidateSituationType.INVITE.equals(validateSituationType)) {
            // 모임 초대할 때 validate
            inviteValidate((Long) target[0], (Accounts) target[1], (Long) target[2]);
        } else if (ValidateSituationType.RESPONSE_EVENTS_INVITE.equals(validateSituationType)) {
            // 모임 초대 반응할 때 validate
            responseEventsInviteValidate((Long) target[0], (Long) target[1]);
        }
    }

    private void updateEventsImagesValidate(UpdateEventsImagesReq target) {
        // events가 존재하는지 체크.
        // 해당 event 주인이 본인이 맞는지 체크.
        validateEventsHost(target.getAccountsId(), checkExistEvents(target.getId()).getAccounts().getId());

        // main ImageType은 하나만 가능
        Set<EventsImagesDto> images = target.getImages();
        if (images != null) {
            checkOnlyOneMainImageType(
                    images.stream()
                        .map(EventsImagesDto::getImagesType)
                        .collect(Collectors.toSet())
            );
        }
    }

    private void validateEventsType(UpdateEventsContentsReq target) {
        if (target.getEventsType().equals(EventsType.OFFLINE)) {
            String address = target.getAddress();
            String latitude = target.getLatitude();
            String longitude = target.getLongitude();
            if (
                    address == null
                    || address.isEmpty()
                    || latitude == null
                    || latitude.isEmpty()
                    || longitude == null
                    || longitude.isEmpty()
            ) {
                throw new TodayAssembleException("OFFLINE > 주소 값 누락", ErrorCode.BAD_REQUEST);
            }
        } else {
            Set<ZoomsDto> zooms = target.getZooms();
            if (zooms == null || zooms.isEmpty()) {
                throw new TodayAssembleException("ONLINE > ZOOM 값 누락", ErrorCode.BAD_REQUEST);
            } else {
                chekZoomsValidate(zooms);
            }
        }
    }

    private void chekZoomsValidate(Set<ZoomsDto> zooms) {
        for (ZoomsDto zoomsDto : zooms) {
            if (zoomsDto.getStatus() == null
                || zoomsDto.getUrl() == null
                || zoomsDto.getUrl().isEmpty()) {
                throw new TodayAssembleException(ErrorCode.BAD_REQUEST_ZOOMS);
            }
        }
    }

    private void deleteEventsValidate(UpdateEventsReqBase target) {

        // events가 존재하는지 체크.
        // 해당 event 주인이 본인이 맞는지 체크.
        validateEventsHost(target.getAccountsId(), checkExistEvents(target.getId()).getAccounts().getId());

    }

    private void updateValidate(UpdateEventsContentsReq updateEventsContentsReq) {

        Events events = Events.of(updateEventsContentsReq);
        events.setId(updateEventsContentsReq.getId());

        // events가 존재하는지 체크.
        // 해당 event 주인이 본인이 맞는지 체크.
        Events existEvents = checkExistEvents(updateEventsContentsReq.getId());
        Accounts eventsAccounts = existEvents.getAccounts();

        validateEventsHost(updateEventsContentsReq.getAccountsId(), eventsAccounts.getId());

        // 기존에 있는 event 시간이랑 겹치는 체크.
        events.setAccounts(eventsAccounts);
        validateEventsTime(events);

        // 모임 type 체크
//        validateEventsType(updateEventsContentsReq);

    }

    private Events checkExistEvents(Long eventsId) {
        return eventsRepository.findById(eventsId)
                .orElseThrow(() -> {
                    throw new TodayAssembleException(ErrorCode.NO_EVENTS_ID);
                });
    }

    private void validateEventsHost(Long accountsId, Long eventsAccountsId) {

        if (!accountsId.equals(eventsAccountsId)) {
            throw new TodayAssembleException(ErrorCode.NOT_EQUAL_ACCOUNT);
        }

    }

    private void createValidate(Events target) {
        // offline일 경우 주소값 필수 체크 , online일 경우 줌 값 필수 체크
//        UpdateEventsContentsReq checkForType = new UpdateEventsContentsReq();
//        checkForType.setEventsType(target.getEventsType());
//        checkForType.setAddress(target.getAddress());
//        checkForType.setLongitude(target.getLongitude());
//        checkForType.setLatitude(target.getLatitude());
//        checkForType.setZooms(target.getZoomsSet().stream()
//                .map(ZoomsDto::from)
//                .collect(Collectors.toSet()));

//        validateEventsType(checkForType);

        // 기존에 있는 event 시간이랑 겹치는 체크.
        validateEventsTime(target);

        // main ImageType은 하나만 가능
        Set<EventsImages> eventsImagesSet = target.getEventsImagesSet();
        if (eventsImagesSet != null) {
            checkOnlyOneMainImageType(
                    eventsImagesSet.stream()
                            .map(Images::getImagesType)
                            .collect(Collectors.toSet())
            );
        }
    }

    private void checkOnlyOneMainImageType(Set<ImagesType> imagesTypes) {
        if (imagesTypes != null) {
            long count = imagesTypes.stream()
                    .filter(ImagesType.MAIN::equals)
                    .count();
            if (count > 1) {
                throw new TodayAssembleException(ErrorCode.OVER_MAIN_IMAGES);
            }
        }
    }

    private void validateEventsTime(Events target) {
        Accounts accounts = target.getAccounts();
        List<Events> eventsList = eventsRepository.findByAccountsId(accounts.getId());

        if (eventsList != null) {
            eventsList.forEach(
                item -> {
                    if (!checkEventTime(item, target) && !item.getId().equals(target.getId())) {
                        throw new TodayAssembleException(ErrorCode.DATE_OVERLAP);
                    }
                });
        }
    }

    private boolean checkEventTime(Events checkEvents, Events events) {
        LocalDateTime eventsStartTime = events.getEventsTime();
        LocalDateTime eventsEndTime = eventsStartTime.plusHours(events.getTakeTime());

        long eventsStartTimeMillSec = getLocalDateTimeToMillSec(eventsStartTime);
        long eventsEndTimeMillSec = getLocalDateTimeToMillSec(eventsEndTime);

        LocalDateTime checkStartTime = checkEvents.getEventsTime();
        LocalDateTime checkEndTime = checkStartTime.plusHours(checkEvents.getTakeTime());

        long checkStartTimeMillSec = getLocalDateTimeToMillSec(checkStartTime);
        long checkEndTimeMillSec = getLocalDateTimeToMillSec(checkEndTime);

        if ((eventsStartTimeMillSec >= checkStartTimeMillSec && eventsStartTimeMillSec <= checkEndTimeMillSec)
            || (eventsEndTimeMillSec >= checkStartTimeMillSec && eventsEndTimeMillSec <= checkEndTimeMillSec)) {
            return false;
        }
        return true;

    }

    private long getLocalDateTimeToMillSec(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    private void participateValidate(Long eventsId) {

        // 인원 수가 가능한지 체크
        checkOverMaxMembers(eventsId);

        // 오늘 날짜 기준 이전 모임은 참가 불가능
        Events events = eventsRepository.findById(eventsId)
                .orElseThrow(() -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID));

        if (events.getEventsTime().isBefore(LocalDateTime.now())) {
            throw new TodayAssembleException(ErrorCode.IMPOSSIBLE_PARTICIPATE_TIME);
        }

    }

    private void checkOverMaxMembers(Long eventsId) {
        Optional<List<AccountsMapperEvents>> optional = accountsEventsRepository.findByEventsId(eventsId);
        int participationMember = (int) optional.orElseThrow(
                () -> new TodayAssembleException(ErrorCode.NO_EVENTS_ID))
                .stream()
                .filter(AccountsMapperEvents::getAccept).count();

        Events events = eventsRepository.findById(eventsId).get();
        int checkMaxMember = (int) events.getMaxMembers();
        if (checkMaxMember <= participationMember) {
            throw new TodayAssembleException(ErrorCode.OVER_MAX_MEMBER);
        }
    }

    private void inviteValidate(Long eventsId, Accounts accounts, Long invitesId) {
        // 호스트가 맞는지 체크
        validateEventsHost(accounts.getId(), checkExistEvents(eventsId).getAccounts().getId());

        // 최대 인원 초과하는지 체크
        checkOverMaxMembers(eventsId);

        // 초대 할려는 계정이 존재하는지 체크
        checkExistInviteAccounts(invitesId);

        // 이미 있는 계정인지 체크
        checkEventsExistAccounts(eventsId, invitesId);
    }

    private void checkExistInviteAccounts(Long invitesId) {
        accountsRepository.findById(invitesId).orElseThrow(
                () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT)
        );
    }

    private void checkEventsExistAccounts(Long eventsId, Long invitesId) {
        if (accountsEventsRepository.findByAccountsIdAndEventsId(invitesId, eventsId).isPresent()) {
            throw new TodayAssembleException(ErrorCode.ALREADY_INVITE_ACCOUNTS);
        }
    }

    private void responseEventsInviteValidate(Long eventsId, Long accountsId) {
        accountsEventsRepository.findByAccountsIdAndEventsId(accountsId, eventsId).orElseThrow(
                () -> new TodayAssembleException(ErrorCode.NO_ACCOUNTS_IN_EVENTS)
        );

    }

}
