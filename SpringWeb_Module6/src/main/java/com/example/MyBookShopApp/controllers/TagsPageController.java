package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.BookService;
import com.example.MyBookShopApp.data.book.BooksPageDto;
import com.example.MyBookShopApp.data.tag.TagDto;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class TagsPageController {
    private final BookService bookService;
    private final TagService tagService;
    @Autowired
    public TagsPageController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }
    @ModelAttribute("active")
    public String activePage() {
        return "Null";
    }



    @GetMapping("/tags/{tagWord}")
    public String getBooksTag(@PathVariable(value = "tagWord") TagDto tagWord, Model model){
        model.addAttribute("TagDto", tagWord);
        TagEntity tag = tagService.findByTagName(tagWord.getTag());
        model.addAttribute("tagBooks", bookService.getPageOfBooksByTag(tag, 0, 10).getContent());
        return "/tags/index";
    }
}
