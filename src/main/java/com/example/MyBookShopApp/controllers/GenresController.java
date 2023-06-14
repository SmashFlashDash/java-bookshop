package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.BookService;
import com.example.MyBookShopApp.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class GenresController {
    private final GenreService genreService;
    private final BookService bookService;
    private final AuthService authService;

    @ModelAttribute("active")
    public String active() {
        return "Genres";
    }

    @GetMapping("/genres/**")
    public String slugPage(HttpServletRequest request, Model model) {
        ArrayList<String> slugs = new ArrayList<>(Arrays.asList(request.getRequestURI().split("/")));
        slugs.removeIf(s -> (s.equals("") || s.equals("genres")));
        List<Genre> genres = genreService.getGenreEntitesBySlugs(slugs);
        model.addAttribute("pathVars", genres.stream().map(Genre::getSlug).collect(Collectors.toList()));
        model.addAttribute("genres", genres);
        model.addAttribute("books", bookService.getPageOfBooksByListGenres(
                genreService.getGenreNodes(slugs.get(slugs.size() - 1)), 0, 10).getContent());
        return "/genres/slug";
    }

    @GetMapping("/genres")
    public String postponedPage(Model model) {
        model.addAttribute("genres", genreService.getAllGenresDto());
        return "/genres/index";
    }
}
