package com.lim.assemble.todayassemble.config;

import com.lim.assemble.todayassemble.accounts.service.impl.AccountsLoginServiceImpl;
import com.lim.assemble.todayassemble.common.filter.AuthenticationFilter;
import com.lim.assemble.todayassemble.common.filter.LoginFilter;
import com.lim.assemble.todayassemble.common.type.AccountsType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountsLoginServiceImpl accountsLoginService;
    private final AuthenticationService authenticationService;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountsLoginService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // todo: url체크 추가해야함.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and().authorizeRequests()
                .antMatchers(HttpMethod.GET
                        , "*"
                        , "/api/v1/events").permitAll()
                .antMatchers(HttpMethod.POST
                        , "/login"
                        , "/api/v1/accounts/sign-up").permitAll()
                .antMatchers(HttpMethod.POST
                        , "/api/v1/events").hasAnyRole(AccountsType.CLIENT.getType(), AccountsType.ADMIN.getType())
                .antMatchers(HttpMethod.PUT
                        , "/api/v1/events").hasAnyRole(AccountsType.CLIENT.getType(), AccountsType.ADMIN.getType())
//                .anyRequest().authenticated()
                .and()
                .addFilterBefore(
                        new LoginFilter("/login", authenticationManagerBean())
                        , UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new AuthenticationFilter(authenticationService)
                        , UsernamePasswordAuthenticationFilter.class
                )
        ;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.applyPermitDefaultValues();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
