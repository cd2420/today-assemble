package com.lim.assemble.todayassemble.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // todo: allowOrigins 부분 동적으로 받아오도록 수정 필요
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**").allowCredentials(true).allowedOrigins("http://localhost:3000");
    }
}
