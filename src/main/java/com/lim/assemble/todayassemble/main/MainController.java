package com.lim.assemble.todayassemble.main;

import com.lim.assemble.todayassemble.events.dto.EventsDto;
import com.lim.assemble.todayassemble.events.service.EventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final EventsService eventsService;

    @GetMapping("/home")
    public ResponseEntity<List<EventsDto>> home(
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        return  ResponseEntity.ok(eventsService.getEventsList(pageable));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(
           HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        return  ResponseEntity.ok().build();
    }

}
