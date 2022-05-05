package com.lim.assemble.todayassemble.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lim.assemble.todayassemble.accounts.dto.AccountsCredentials;
import com.lim.assemble.todayassemble.config.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String url, AuthenticationManager authenticationManager) {
        super(url);
        setAuthenticationManager(authenticationManager);
    }

    /**
     * SecurityConfig > configureGlobal 에서 커스텀한 내용을 가지고 유저 인증.
     * userDetailsService 가 구현한 loadUserByUsername 로 값 비교하여 인증처리.
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException
    {
        AccountsCredentials accountsCredentials = new ObjectMapper()
                                                        .readValue(
                                                            req.getInputStream(), AccountsCredentials.class
                                                        );

        // DaoAuthenticationProvider > retrieveUser 에서 설정한 UserDetailsService 사용하여 원래 데이터와 요청 데이터 비교.
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountsCredentials.getEmail()
                        , accountsCredentials.getPassword()
                        , Collections.emptyList()
                )
        );
    }

    /**
     * Clients 에 jwt 발행
     * @param req
     * @param res
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(
            HttpServletRequest req
            , HttpServletResponse res
            , FilterChain chain
            , Authentication auth) throws IOException, ServletException {
        AuthenticationService.addJWTToken(res, auth.getName());
    }
}
