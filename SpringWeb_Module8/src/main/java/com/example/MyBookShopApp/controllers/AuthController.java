package com.example.MyBookShopApp.controllers;


import com.example.MyBookShopApp.dto.*;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.TokenService;
import com.example.MyBookShopApp.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse requestContactConfirmation(
            @RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse approveContact(
            @RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String registration(RegistrationForm registrationForm, Model model) {
        authService.registerNewOrGetUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse login(@RequestBody ContactConfirmationPayload payload,
                                             HttpServletResponse httpServletResponse) {
        ContactConfirmationResponse loginResponse = authService.jwtLogin(payload);
        Cookie cookie = new Cookie("token", loginResponse.getResult());
        httpServletResponse.addCookie(cookie);
        return loginResponse;
    }

    @GetMapping("/my")
    public String handleMy(@AuthenticationPrincipal OAuth2User principal, Model model) {
        // TODO: можно было взять @AuthenticationPrincipal, но берется в сервисе
        model.addAttribute("curUsr", new UserDto(authService.getCurrentUser()));
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", new UserDto(authService.getCurrentUser()));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam Map<String,String> params, Model model) {
        // TODO: надо разобрать данные и сейвить в таблицу
        ProfileUpdateRequest request = objectMapper.convertValue(params, ProfileUpdateRequest.class);
        ProfileUpdateResponse response = userService.updateUser(request);
        model.addAttribute("curUsr", new UserDto(response.getUser()));
        model.addAttribute("updated", true);
        if (response.getError() != null) {
            model.addAttribute("error", response.getError());
        }
        return "/profile";
    }

//    @GetMapping("/logout")
//    public String handleLogout(@CookieValue(value = "token", required = false) Cookie token) {
//        HttpSession session = request.getSession();
//        SecurityContextHolder.clearContext();
//        if (session != null) {
//            session.invalidate();
//        }
//        for (Cookie cookie : request.getCookies()) {
//            cookie.setMaxAge(0);
//        }
//        return "redirect:/";
//    }
}
