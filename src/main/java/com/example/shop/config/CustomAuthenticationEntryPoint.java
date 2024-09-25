package com.example.shop.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException{

        //ajax의경우 http request header에 XMLHttpRequest라는 값이 세팅되어 요청
        if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            //인증되지않은 사용자가 ajax로 리소스 요청할경우 unauthorized 에러를 발생
        } else {
            response.sendRedirect("/members/login"); // 그외의경우 로그인페이지로 리다이렉트
        }
    }
}
