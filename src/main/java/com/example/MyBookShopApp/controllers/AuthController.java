package com.example.MyBookShopApp.controllers;


import com.example.MyBookShopApp.dto.*;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.UserService;
import com.example.MyBookShopApp.sms.SmsCode;
import com.example.MyBookShopApp.sms.SmsService;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SmsService smsService;

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
    public ContactConfirmationResponse requestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");

        if (payload.getContact().contains("@")) {
            return response;    // заглуша на email
        } else {
            String smsCode = smsService.sendSecretCodeSms(payload.getContact());
            smsService.saveNewCode(new SmsCode(smsCode, 60));   // expire in 1 min
            return response;
        }
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse approveContact(
            @RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();

         if (smsService.verifyCode(payload.getCode())) {
             response.setResult("true");
         } else {
             if (payload.getContact().contains("@")) {
                 response.setResult("true");
             } else {
                 response.setResult("false");
             }
         }
        return response;
    }

    @PostMapping("/reg")
    public String registration(RegistrationForm registrationForm, Model model) {
        authService.registerNewOrGetUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/login-by-phone-number")
    @ResponseBody
    public ContactConfirmationResponse loginByPhone(@RequestBody ContactConfirmationPayload payload,
                                             HttpServletResponse httpServletResponse) {
        if (smsService.verifyCode(payload.getCode())) {
            ContactConfirmationResponse loginResponse = authService.jwtLoginByPhone(payload);
            Cookie cookie = new Cookie("token", loginResponse.getResult());
            httpServletResponse.addCookie(cookie);
            return loginResponse;
        } else {
            return null;
        }
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
        model.addAttribute("curUsr", new UserDto(authService.getCurrentUser().get()));
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", new UserDto(authService.getCurrentUser().get()));
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@RequestParam Map<String, String> params, Model model) {
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
