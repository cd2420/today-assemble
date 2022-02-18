package com.lim.assemble.todayassemble.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    @GetMapping("/home")
    public String home(HttpServletRequest request) {
        log.info("url: {}", request.getRequestURI());
        return "hello world!!!!!!!";
    }

}
