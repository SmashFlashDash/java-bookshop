package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BookStatusDto;
import com.example.MyBookShopApp.dto.ChangeBookStatusRequest;
import com.example.MyBookShopApp.services.BookRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

@RestController
@RequiredArgsConstructor
public class ChangeBookStatusController {
    private final BookRatingService bookRatingService;

    @ResponseBody
    @PostMapping("/books/changeBookStatus/{slug}")
    public ResponseEntity<BookStatusDto> handleChangeBookStatus(@PathVariable(name = "slug", required = false) String slug,
                                                                @CookieValue(name = "postContents", required = false) Cookie postContents,
                                                                @CookieValue(name = "cartContents", required = false) Cookie cartContents,
                                                                @RequestBody Map<String, String> status,
                                                                HttpServletRequest request, HttpServletResponse response, Model model) {
        switch (status.get("status")) {
            case "KEPT":
                removeBookFromCookie(cartContents, slug, response);
                addBookToCookie(postContents, "postContents", slug, response);
                return ResponseEntity.ok(new BookStatusDto(true));
            case "CART":
                removeBookFromCookie(postContents, slug, response);
                addBookToCookie(cartContents, "cartContents", slug, response);
                response.getHeaderNames();
                return ResponseEntity.ok(new BookStatusDto(true));
            case "UNLINK":
                removeBookFromCookie(postContents, slug, response);
                removeBookFromCookie(cartContents, slug, response);
                return ResponseEntity.ok(new BookStatusDto(true));
            default:
                return ResponseEntity.ok(new BookStatusDto(false));
        }
    }

    @ResponseBody
    @PostMapping("/books/changeBookStatus")
    public ResponseEntity<BookStatusDto> handleRating(@CookieValue(name = "postContents", required = false) Cookie postContents,
                                                      @CookieValue(name = "cartContents", required = false) Cookie cartContents,
                                                      @RequestBody ChangeBookStatusRequest status, HttpServletResponse response, Model model) {
        bookRatingService.guestSetRaitingToBokk(status.getBookId(), status.getValue());
        return ResponseEntity.ok(new BookStatusDto(true));
    }


    private void removeBookFromCookie(Cookie toRemoveCookie, String slug, HttpServletResponse response) {
        if (toRemoveCookie != null && toRemoveCookie.getValue().contains(slug)) {
            ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(toRemoveCookie.getValue().split("/")));
            cookieBooks.remove(slug);
            Cookie newCookie = new Cookie(toRemoveCookie.getName(), String.join("/", cookieBooks));
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }

    private void addBookToCookie(Cookie toAddCookie, String addCookieName, String slug, HttpServletResponse response) {
        if (toAddCookie == null || toAddCookie.getValue().equals("")) {
            Cookie newCookie = new Cookie(addCookieName, slug);
            newCookie.setPath("/");
            response.addCookie(newCookie);
        } else if (!toAddCookie.getValue().contains(slug)) {
            StringJoiner stringJoiner = new StringJoiner("/");
            stringJoiner.add(toAddCookie.getValue()).add(slug);
            Cookie newCookie = new Cookie(toAddCookie.getName(), stringJoiner.toString());
            newCookie.setPath("/");
            response.addCookie(newCookie);
        }
    }
}
