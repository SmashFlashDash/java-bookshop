package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LogoutHandlerImpl implements LogoutHandler {
    private final TokenService tokenService;

    @Autowired
    public LogoutHandlerImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                tokenService.saveJwtBlackList(cookie.getValue());
                break;
            }
        }
    }
}
