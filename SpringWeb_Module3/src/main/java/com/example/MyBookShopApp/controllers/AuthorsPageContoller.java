package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authors")
public class AuthorsPageContoller {
    private final AuthorService authorService;

    @Autowired
    public AuthorsPageContoller(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("")
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "/authors/index";
    }
}



