package com.lim.assemble.todayassemble.main;

import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final EventsService eventsService;

    @GetMapping("/home")
    public ResponseEntity<List<EventsDto>> home(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC) Pageable pageable
            , HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        return  ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
           HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        SecurityContextHolder.clearContext();
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventsDto>> searchEventsList(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC) Pageable pageable
            , HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");

        return  ResponseEntity.ok(eventsService.searchEventsList(keyword, pageable));
    }

    @GetMapping("/search/size")
    public ResponseEntity<Integer> searchEventsSize(
            HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");

        return  ResponseEntity.ok(eventsService.searchEventsSize(keyword));
    }

    @GetMapping("/search/place")
    public ResponseEntity<List<EventsDto>> searchEventsListByPlace(
            @PageableDefault(size = 9, sort = "eventsTime", direction = Sort.Direction.ASC) Pageable pageable
            , HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");
        return  ResponseEntity.ok(eventsService.searchEventsListByPlace(keyword, pageable));
    }

    @GetMapping("/search/place/size")
    public ResponseEntity<Integer> searchEventsSizeByPlace(
            HttpServletRequest request) throws UnsupportedEncodingException {
        log.info("url: {}", request.getRequestURI());
        String keyword = URLDecoder.decode(request.getParameter("keyword"), "UTF-8");

        return  ResponseEntity.ok(eventsService.searchEventsSizeByPlace(keyword));
    }

}
