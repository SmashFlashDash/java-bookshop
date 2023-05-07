package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostponedController {

    private final BookRepository bookRepository;

    @Autowired
    public PostponedController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @ModelAttribute(name = "bookPostponed")
    public List<Book> bookPostponed() {
        return new ArrayList<>();
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
