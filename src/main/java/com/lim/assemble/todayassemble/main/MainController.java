package com.lim.assemble.todayassemble.main;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final EventsService eventsService;

    @ApiOperation(value = "메인 화면에 나타낼 모임 리스트 조회", notes = "1.페이징 옵션 필요\n2.지금 시간 이후 모임 시작 시간인 모임만 조회")
    @GetMapping("/home")
    public ResponseEntity<List<EventsDto>> home(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC) Pageable pageable
            , HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        return  ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @ApiOperation(value = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
           HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        SecurityContextHolder.clearContext();
        return  ResponseEntity.ok().build();
    }

    @ApiOperation(value = "모임 검색")
    @GetMapping("/search")
    public ResponseEntity<List<EventsDto>> searchEventsList(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC) Pageable pageable
            , @ApiParam(value = "검색 키워드 - 태그 or 모임명") @RequestParam String keyword
            , HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
//        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
        keyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);

        return  ResponseEntity.ok(eventsService.searchEventsList(keyword, pageable));
    }

    @ApiOperation(value = "모임 검색 사이즈", notes = "검색 후 리스트 화면에서 pagination 표시하기 위해 구함")
    @GetMapping("/search/size")
    public ResponseEntity<Integer> searchEventsSize(
            @ApiParam(value = "검색 키워드 - 태그 or 모임명") @RequestParam String keyword
            , HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
//        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
        keyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        return  ResponseEntity.ok(eventsService.searchEventsSize(keyword));
    }

    @ApiOperation(value = "모임 검색 - 장소", notes = "지역별 모임 검색 화면에서 사용됨")
    @GetMapping("/search/place")
    public ResponseEntity<List<EventsDto>> searchEventsListByPlace(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC) Pageable pageable
            , @ApiParam(value = "검색 키워드 - 주소") @RequestParam String keyword
            , HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
//        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
        keyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        return  ResponseEntity.ok(eventsService.searchEventsListByPlace(keyword, pageable));
    }

    @ApiOperation(value = "모임 검색 사이즈 - 장소", notes = "검색 후 리스트 화면에서 pagination 표시하기 위해 구함")
    @GetMapping("/search/place/size")
    public ResponseEntity<Integer> searchEventsSizeByPlace(
            @ApiParam(value = "검색 키워드 - 주소") @RequestParam String keyword
            , HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
//        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
        keyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        return  ResponseEntity.ok(eventsService.searchEventsSizeByPlace(keyword));
    }

}
