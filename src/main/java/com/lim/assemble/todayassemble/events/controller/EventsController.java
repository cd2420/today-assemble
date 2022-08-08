package com.lim.assemble.todayassemble.events.controller;

import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {

    private final EventsService eventsService;

    @ApiOperation(value = "모임 리스트 조회", notes = "1.페이징 옵션 필요\n2.지금 시간 이후 모임 시작 시간인 모임만 조회")
    @GetMapping("")
    public ResponseEntity<List<EventsDto>> getEventsList(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC)
                    Pageable pageable
            , HttpServletRequest request
            ) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @ApiOperation(value = "모임 리스트 사이즈", notes = "1.리스트 화면에서 pagination 표시하기 위해 구함\n2.지금 시간 이후 모임 시작 시간인 모임만 조회")
    @GetMapping("/size")
    public ResponseEntity<Integer> getEventsListSize(HttpServletRequest request) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getEventsListSize());
    }

    @ApiOperation(value = "모임 상세 조회")
    @GetMapping("{eventId}")
    public ResponseEntity<EventsDto> getEventsDetail(
            @ApiParam(value = "조회할 모임 id(seq)") @PathVariable Long eventId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getEvents(eventId));
    }

    @ApiOperation(value = "모임에 참여중인 사람수")
    @GetMapping("{eventId}/accounts")
    public ResponseEntity<Integer> getParticipateEventsAccountsSize(
            @ApiParam(value = "조회할 모임 id(seq)") @PathVariable Long eventId
            , HttpServletRequest request
    ) {
        log.info("api : {}" , request.getRequestURI());
        return ResponseEntity.ok(eventsService.getParticipateEventsAccountsSize(eventId));
    }
}
