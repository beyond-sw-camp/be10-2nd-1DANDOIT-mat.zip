package com.matzip.matzipuser.sercurity.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.matzipuser.sercurity.dto.ExceptionResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");

        response.getWriter().write(new ObjectMapper().writeValueAsString(new ExceptionResponseDTO(401, "로그인 실패")));
    }
}
