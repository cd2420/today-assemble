package com.lim.assemble.todayassemble.config;


import com.lim.assemble.todayassemble.accounts.dto.UserAccount;
import com.lim.assemble.todayassemble.accounts.service.impl.AccountsLoginServiceImpl;
import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountsLoginServiceImpl accountsLoginService;

    static final long EXPIRATION_TIME = 864_000_00;
    static final String SIGNING_KEY = "signingKey";
    static final String BEARER_PREFIX = "Bearer";

    static public void addJWTToken(HttpServletResponse response, String email) {
        String jwtToken = Jwts.builder().setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();
        response.addHeader("Authorization" , BEARER_PREFIX + " " + jwtToken);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null){
            String user = Jwts.parser()
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                UserAccount userAccount = (UserAccount) accountsLoginService.loadUserByUsername(user);
                return new UsernamePasswordAuthenticationToken(userAccount, "", userAccount.getAuthorities());
            } else {
                throw new TodayAssembleException(ErrorCode.FAILED_AUTHENTICATION);
            }

        }

        return null;
    }

}
