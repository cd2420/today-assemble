package com.lim.assemble.todayassemble.accounts.interceptor;

import com.lim.assemble.todayassemble.exception.ErrorCode;
import com.lim.assemble.todayassemble.exception.TodayAssembleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


@Slf4j
public class AccountsInterceptor implements HandlerInterceptor {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean preHandle(
            HttpServletRequest request
            , HttpServletResponse response
            , Object handler
    ) {

        // request Body 값 추출
        try {
            String body = getRequestBodyWhenRelatedAccounts(request).toString();
            log.info("$$$$$$$$$$$ requestBody: {}  $$$$$$$$$$$$$$$$$", body);
            return true;
        } catch (Exception e) {
            throw new TodayAssembleException(ErrorCode.NO_REQUEST_BODY);
        }

    }

    private StringBuilder getRequestBodyWhenRelatedAccounts(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        return stringBuilder;
    }
}
