package com.lim.assemble.todayassemble.config;

import com.lim.assemble.todayassemble.accounts.service.AccountsService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountsService accountsService;
    private final AuthenticationService authenticationService;

    /**
     * (SecurityContextHolder 에 Authentication 담겨있음)
     * Authentication 만들고 인증처리하는 인터페이스 = AuthenticationManager
     * AuthenticationManagerDelegator 를 AuthenticationManager 로 컨테이너에 등록
     * @return AuthenticationManagerDelegator
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * AuthenticationManagerBuilder 를 컨테이너로부터 주입받고 AuthenticationManager 의 인증 설정 커스텀.
     * 1. 사용자 확인하는 인터페이스(UserDetailsService)가 구현된 클래스 설정.
     *    => 사용자 확인이 필요할 경우 여기에 구현된 기능을 가지고 확인을 한다.
     * 2. 가져온 UserDetails 에 있는 Password 를 암호화 시킴.
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * http 통신에 관한 설정
     * 1. cors - 다른 도메인의 resource 사용을 막는 정책
     * 2. csrf - 사이트간 위조 요청 방지
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()     // csrf disable 하는 이유? rest api 서버인 경우 session 기반 인증과는 다르게 stateless 하기 때문에 서버에 인증정보를 보관하지 않기 때문에 굳이 불필요한 csrf에 관한 코드가 작성할 필요가 없다.
                .cors().configurationSource(corsConfigurationSource())   // cors 설정
                .and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET
                            , "/api/v1/home"
                            , "/api/v1/events").permitAll()
                    .antMatchers(HttpMethod.POST
                            , "/login"
                            , "/api/v1/accounts/sign-up").permitAll()
                    .antMatchers(HttpMethod.GET
                            , "/api/v1/accounts/likes/events").hasAnyRole(AccountsType.CLIENT.getType(), AccountsType.ADMIN.getType())
                    .antMatchers(HttpMethod.POST
                            , "/api/v1/events/**"
                            , "/api/v1/accounts/*/events").hasAnyRole(AccountsType.CLIENT.getType(), AccountsType.ADMIN.getType())
                    .antMatchers(HttpMethod.PUT
                            , "/api/v1/events/**"
                            , "/api/v1/accounts/**").hasAnyRole(AccountsType.CLIENT.getType(), AccountsType.ADMIN.getType())
                    .antMatchers(HttpMethod.DELETE
                            , "/api/v1/events/**").hasAnyRole(AccountsType.CLIENT.getType(), AccountsType.ADMIN.getType())
                .and()
                    .addFilterBefore(
                        new LoginFilter("/login", authenticationManagerBean())
                        , UsernamePasswordAuthenticationFilter.class
                    )
                    .addFilterBefore(
                        new AuthenticationFilter(authenticationService)
                        , UsernamePasswordAuthenticationFilter.class
                    )
                .logout()
                    .logoutSuccessUrl("/api/v1/logout")
        ;
    }


    /**
     * cors 정책 설정 함수
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
