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
//    Дерево жанров технически представляет собой блок ссылок, а вложенность достигается путём
//    включения одних блоков и групп блоков в другие parent-блоки.
//    Каждая ссылка в блоке ведёт на страницу /genres/slug.html, в которой при помощи
//    механизма пагинации осуществляется вывод книг, обладающих выбранным из облака жанром.
//    Для реализации этой задачи вам понадобится хранить данные по жанрам (список жанров)
//    и осуществлять их мэппинг с данными из таблицы books. Далее, выполняя запрос на выборку книг
//    с определённым жанром и анализируя результаты, вы сможете определять контент — список книг из
//    books для вывода на странице сайта при помощи уже имеющегося в вашем распоряжении механизма
//    пагинации контента.
//    Рендеринг дерева жанров может быть осуществлён средствами Thymeleaf или JQuery на ваш выбор.

    // select * where id =, and parent_id =
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
        String[] _slugs = request.getRequestURI().split("/");
        List<String> slugs = IntStream.range(0, _slugs.length).filter(i -> i > 1).mapToObj(i -> _slugs[i]).collect(Collectors.toList());
        List<GenreEntity> genres = genreService.getGenresBySlugsArray(slugs);
        model.addAttribute("pathVars", genres.stream().map(g -> g.getSlug()).collect(Collectors.toList()));
        model.addAttribute("genres", genres);
        // model.addAttribute("pathVars",
        //         IntStream.range(0, _slugs.length).filter(i -> i > 1).mapToObj(i -> _slugs[i]).collect(Collectors.toList()));
        // Сначала надо получить список всех slugs потом взять книжки
        // или рекурсивный sql
        // сюда надо кидать names а не slugs, а получить их по slugs из адреса
        GenreEntity genre = genreService.getGenreEntityBySlug(slugs.get(slugs.size() - 1));
        model.addAttribute("genre", genre);
        model.addAttribute("books", bookService.getPageOfBooksByGenre(genre, 0, 10).getContent());
        return "/genres/slug";
    }

    @GetMapping("/genres")
    public String postponedPage(Model model){
        model.addAttribute("genres", genreService.getAllGenresDto());
        return "/genres/index";
    }
}
