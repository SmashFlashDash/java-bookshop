package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BookStatusDto;
import com.example.MyBookShopApp.services.BookRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

@RestController
public class ChangeBookStatusController {
    private final BookRatingService bookRatingService;

    @Autowired
    public ChangeBookStatusController(BookRatingService bookRatingService) {
        this.bookRatingService = bookRatingService;
    }


    @ResponseBody
    @PostMapping("/books/changeBookStatus/{slug}")
    public ResponseEntity<BookStatusDto> handleChangeBookStatus(@PathVariable(name = "slug", required = false) String slug,
                                                                @CookieValue(name = "postContents", required = false) Cookie postContents,
                                                                @CookieValue(name = "cartContents", required = false) Cookie cartContents,
                                                                HttpServletRequest request, HttpServletResponse response, Model model) {
        String status = request.getParameter("status");
        switch (status) {
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
                // TODO: вообще exception еще есть ARCHIVED
                return ResponseEntity.ok(new BookStatusDto(false));
        }
    }

    // TODO: при установке рейтинга книге сохранить в куки книгу=значение рейтинга
    //  и при доабвлении отзыва, если у этой книги есть этот куки то взять его id
    //  или взять из модели у Dom обьекта значие
    @ResponseBody
    @PostMapping("/books/changeBookStatus")
    public ResponseEntity<BookStatusDto> handleRating(@CookieValue(name = "postContents", required = false) Cookie postContents,
                                                      @CookieValue(name = "cartContents", required = false) Cookie cartContents,
                                                      HttpServletRequest request, HttpServletResponse response, Model model) {
        Short value = Short.parseShort(request.getParameter("value"));
        Integer bookId = Integer.parseInt(request.getParameter("bookId"));
        bookRatingService.guestSetRaitingToBokk(bookId, value);
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
