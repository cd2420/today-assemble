package com.lim.assemble.todayassemble.config;

import com.lim.assemble.todayassemble.accounts.interceptor.AccountsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

//    @Bean
//    public AccountsInterceptor makeAccountsInterceptor() {
//        return new AccountsInterceptor();
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(makeAccountsInterceptor())
//                .addPathPatterns("/api/v1/accounts/sign-up", "api/v1/accounts/login")
//        ;

    }
}
