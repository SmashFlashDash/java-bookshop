package com.example.MyBookShopApp.security.jwt;

import com.example.MyBookShopApp.controllers.GlobalExceptionHandlerController;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.security.UserDetailsServiceImpl;
import com.example.MyBookShopApp.services.TokenService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final GlobalExceptionHandlerController handlerController;

    public JWTRequestFilter(UserDetailsServiceImpl userDetailsService, JWTUtil jwtUtil, TokenService tokenService, GlobalExceptionHandlerController handlerController) {
        this.bookstoreUserDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
        this.handlerController = handlerController;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    if (tokenService.isJwtInBlackList(cookie.getValue())) continue;
                    String token = cookie.getValue();
                    try {
                        // TODO: если изменить jwt jwtUtl бросает Exception надо обрабатывать?
                        String username = jwtUtil.extractUsername(token);
                        // TODO: authentification можно убрать или это для Oauth?
                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                            UserDetailsImpl userDetails = (UserDetailsImpl) bookstoreUserDetailsService.loadUserByUsername(username);
                            if (jwtUtil.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        }
                    } catch (JwtException e) {
                        handlerController.handleAuthentificationException(e);
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

