package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.BookService;
import com.example.MyBookShopApp.data.genre.GenreDto;
import com.example.MyBookShopApp.data.genre.GenreEntity;
import com.example.MyBookShopApp.data.genre.GenreService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
//@RequestMapping("/genres")
public class GenresPageController {
    GenreService genreService;
    BookService bookService;

    @Autowired
    public GenresPageController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ModelAttribute("active")
    public String active(){
        return "Genres";
    }

    @GetMapping("/genres/**")
    public String slugPage(HttpServletRequest request, Model model){
        ArrayList<String> slugs = new ArrayList<>(Arrays.asList(request.getRequestURI().split("/")));
        slugs.removeIf(s -> (s.equals("") || s.equals("genres")));
        List<GenreEntity> genres = genreService.getGenreEntitesBySlugs(slugs);
        model.addAttribute("pathVars", genres.stream().map(GenreEntity::getSlug).collect(Collectors.toList()));
        model.addAttribute("genres", genres);
        model.addAttribute("books", bookService.getPageOfBooksByListGenres(
                genreService.getGenreNodes(slugs.get(slugs.size()-1)), 0, 10).getContent());
        return "/genres/slug";
    }

    @GetMapping("/genres")
    public String postponedPage(Model model){
        model.addAttribute("genres", genreService.getAllGenresDto());
        return "/genres/index";
    }
}
