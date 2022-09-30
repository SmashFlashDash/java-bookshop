package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksPageController {

    private final BookService bookService;

    @Autowired
    private BooksPageController(BookService bookService){
        this.bookService = bookService;
    }


    @GetMapping("/recent")
    public String recentPage(Model model){
        model.addAttribute("recentBooks", bookService.getBooksData());
        return "/books/recent";
    }

    @GetMapping("/popular")
    public String popularPage(Model model){
        model.addAttribute("popularBooks", bookService.getBooksData());
        return "/books/popular";
    }
}
