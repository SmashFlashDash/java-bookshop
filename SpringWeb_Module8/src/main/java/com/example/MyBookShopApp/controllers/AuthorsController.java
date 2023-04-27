package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.services.AuthorService;
import com.example.MyBookShopApp.services.BookService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
@Api("authors data")  // @Api чтобы класс попал в докуметаци
public class AuthorsController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

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
        // возвращать обьекты authors мб сделать pagebla
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "/authors/index";
    }

    // добавим этот метод чтобы он попал в документацию API
    @GetMapping("/api/authors")
    public Map<String, List<Author>> authors() {
        return authorService.getAuthorsMap();
    }
}
