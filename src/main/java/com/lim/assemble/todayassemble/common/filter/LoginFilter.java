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

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException
    {
        AccountsCredentials accountsCredentials = new ObjectMapper()
                                                        .readValue(
                                                            req.getInputStream(), AccountsCredentials.class
                                                        );
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountsCredentials.getEmail()
                        , accountsCredentials.getPassword()
                        , Collections.emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req
            , HttpServletResponse res
            , FilterChain chain
            , Authentication auth) throws IOException, ServletException {
        AuthenticationService.addJWTToken(res, auth.getName());
    }
}
