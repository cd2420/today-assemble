package com.lim.assemble.todayassemble.config;

import com.lim.assemble.todayassemble.common.property.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AppProperties appProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**").allowCredentials(true).allowedOrigins(appProperties.getFront_host());
    }
}
