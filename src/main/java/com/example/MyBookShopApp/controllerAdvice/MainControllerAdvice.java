package com.example.MyBookShopApp.controllerAdvice;

import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.dto.UserDto;
import com.example.MyBookShopApp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
@RequiredArgsConstructor
public class MainControllerAdvice {
    private final AuthService authService;
    private final BookRepository bookRepository;

    @ModelAttribute("curUsr")
    public UserDto currentUser() {
        return authService.getCurrentUser().map(UserDto::new).orElse(null);
    }

    @ModelAttribute("bookCartCount")
    public Integer booksCart(@CookieValue(name = "cartContents", required = false) String cartContents, Principal principal) {
        return splitCookie(cartContents).length;
    }

    @ModelAttribute("bookPostponedCount")
    public Integer booksPostponed(@CookieValue(name = "postContents", required = false) String postContents, Principal principal) {
        return splitCookie(postContents).length;
    }

    private String[] splitCookie(String cookie) {
        if (cookie == null || cookie.equals("")) {
            return new String[]{};
        }
        cookie = StringUtils.strip(cookie, "/");
        return cookie.split("/");
    }
}
