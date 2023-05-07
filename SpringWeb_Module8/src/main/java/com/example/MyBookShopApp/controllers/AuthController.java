package com.example.MyBookShopApp.controllers;


import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.services.TokenService;
import com.example.MyBookShopApp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

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
        authService.registerNewUser(registrationForm);
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

    // TODO: в getCurrentUser не может кастануть OAuth 2 UserDetails
    @GetMapping("/my")
    public String handleMy(@AuthenticationPrincipal OAuth2User principal, Model model) {
        //  и сделать loginoauth endpoint где он кладет principal в бд, а потом мы берем этого юзера в getCurrentUser
        // TODO: сначала на oauth нужен сервис по успешной регистрироватьт пользоватлея
        // в локлаьной бд и зменить пользоватлея в аунтентификации
        // .oauth2Login().userInfoEndpoint().oidcUserService(customOidcUserService);
        //  TOOD: либо бин сервис определдлить
        //  либо заимплеменить класс UserDetauls Oauth2User
        model.addAttribute("curUsr", authService.getCurrentUser());
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", authService.getCurrentUser());
        return "profile";
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
