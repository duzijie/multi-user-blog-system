package com.yilan.blog.config.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write("{\"code\": \"201\", \"msg\": \"登录成功\", \"data\": " + authentication.getPrincipal() + " }");

        System.out.println("Authorities========");
        System.out.println(authentication.getAuthorities());
        System.out.println("Credentials========");
        System.out.println(authentication.getCredentials());
        System.out.println("Details========");
        System.out.println(authentication.getDetails());
        System.out.println("Principal========");
        System.out.println(authentication.getPrincipal());
    }
}
