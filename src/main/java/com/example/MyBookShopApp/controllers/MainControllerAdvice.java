package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.UserDto;
import com.example.MyBookShopApp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class MainControllerAdvice {
    private final AuthService authService;

    @ModelAttribute("curUsr")
    public UserDto currentUser() {
        return authService.getCurrentUser().map(UserDto::new).orElse(null);
    }
}
