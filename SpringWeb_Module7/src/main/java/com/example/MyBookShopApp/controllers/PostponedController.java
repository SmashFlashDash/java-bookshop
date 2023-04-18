package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.dto.BookStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
public class PostponedController {

    private final BookRepository bookRepository;

    @ModelAttribute(name = "bookPostponed")
    public List<Book> bookPostponed() {
        return new ArrayList<>();
    }

    @Autowired
    public PostponedController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("postponed")
    public String postponedPage(@CookieValue(name = "postContents", required = false) String postContents,
                                Model model) {
        if (postContents == null || postContents.equals("")) {
            model.addAttribute("isCartEmpty", true);
        } else {
            model.addAttribute("isCartEmpty", false);
            postContents = postContents.startsWith("/") ? postContents.substring(1) : postContents;
            postContents = postContents.endsWith("/") ? postContents.substring(0, postContents.length() - 1) : postContents;
            String[] cookieSlugs = postContents.split("/");
            List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);
            model.addAttribute("bookPostponed", booksFromCookieSlugs);
        }
        return "postponed";
    }
}
