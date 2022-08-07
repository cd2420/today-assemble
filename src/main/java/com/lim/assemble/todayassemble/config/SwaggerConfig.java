package com.lim.assemble.todayassemble.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Today Assemble API")
                .description("Today Assemble API Docs").build();
    }

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/**"))
//                .apis(RequestHandlerSelectors.basePackage("com.lim.assemble.todayassemble.accounts.controller"))
//                .apis(RequestHandlerSelectors.basePackage("com.lim.assemble.todayassemble.email.controller"))
//                .apis(RequestHandlerSelectors.basePackage("com.lim.assemble.todayassemble.events.controller"))
//                .apis(RequestHandlerSelectors.basePackage("com.lim.assemble.todayassemble.main"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }
}
