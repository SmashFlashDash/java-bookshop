package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
@Api("authors data")
@RequiredArgsConstructor
public class AuthorsController {
    private final AuthorService authorService;
    private final BookService bookService;
    private final AuthService authService;

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap() {
        return authorService.getAuthorsMap();
    }

    @ModelAttribute("active")
    public String active() {
        return "Authors";
    }

    @GetMapping("/authors/{slug}")
    public String authorsPage(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorBySlug(slug);
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.getPageOfBooksByAuthor(author, 0, 10).getContent());
        return "/authors/slug";
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "/authors/index";
    }

    @GetMapping("/api/authors")
    public Map<String, List<Author>> authors() {
        return authorService.getAuthorsMap();
    }
}
