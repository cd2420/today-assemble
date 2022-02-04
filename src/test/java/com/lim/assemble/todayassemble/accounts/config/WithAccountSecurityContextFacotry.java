package com.lim.assemble.todayassemble.accounts.config;

import com.lim.assemble.todayassemble.accounts.dto.CreateAccountReq;
import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.accounts.repository.AccountsRepository;
import com.lim.assemble.todayassemble.accounts.service.AccountsService;
import com.lim.assemble.todayassemble.accounts.service.impl.AccountsLoginServiceImpl;
import com.lim.assemble.todayassemble.common.type.Gender;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
public class WithAccountSecurityContextFacotry implements WithSecurityContextFactory<WithAccount> {

    private final AccountsService accountService;
    private final AccountsLoginServiceImpl accountsLoginService;
    private final AccountsRepository accountsRepository;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String name = withAccount.value();

        CreateAccountReq createAccountReq = new CreateAccountReq();
        createAccountReq.setEmail("cd2420@naver.com");
        createAccountReq.setPassword("asdfasdf");
        createAccountReq.setName(name);
        createAccountReq.setGender(Gender.MALE);
        createAccountReq.setAge(30);
        createAccountReq.setBirth(LocalDateTime.now());
        accountService.signUp(createAccountReq);

        Accounts accounts = accountsRepository.findByEmail(createAccountReq.getEmail())
                .orElseThrow( () -> new TodayAssembleException(ErrorCode.NO_ACCOUNT));
        accounts.setEmailVerified(true);
        accountsRepository.flush();

        UserDetails principal = accountsLoginService.loadUserByUsername(createAccountReq.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    public static String getJwtToken() {
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final long EXPIRATION_TIME = 864_000_00;
        final String SIGNING_KEY = "signingKey";

        return Jwts.builder().setSubject(userAccount.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
    }
}
