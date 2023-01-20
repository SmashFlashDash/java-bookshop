package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.BookService;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;


// TODO: GenreService лучше вынести триггер в БД при добаавлении в book2genre изменять поле countBooks в genreEntity
// вместо того чтобы кажыдй раз получать все книги чтобы посчитать кол-во книг для жанра
// public GenreDto getAllGenresDto()


@Controller
public class MainPageController {
    private final BookService bookService;
    private final TagService tagService;
    @Autowired
    public MainPageController(BookService bookService, TagService tagService) {
        this.bookService = bookService;
        this.tagService = tagService;
    }
    @ModelAttribute("active")
    public String activePage() {
        return "Main";
    }
    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }
    @ModelAttribute("newBooks")
    public List<Book> newBooks() {
        return bookService.getPageOfNewBooks(0, 6).getContent();
    }
    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookService.getPageOfPopularBooks(0, 6).getContent();
    }
    @ModelAttribute("tagsBooks")
    public List<TagEntity> tagsBooks(){
        // @JsonIgnore в Entity или Jackson StackOverFlow Error из-за цикличного запроса
        // или возвращать DTO
        return tagService.findAllSortedByBooksCount();
    }
//    Задание 3. Реализация страницы автора
//
//    Что нужно сделать
//    У каждого автора в интернет-магазине книг есть личная страница.
//    Вы можете перейти на неё из раздела «Авторы», найдя писателя в алфавитном списке.
//    На личной странице автора есть его фотография и краткая биография.
//    Внизу страницы показан список книг, принадлежащих автору.
//    Список необходимо выводить постранично на случай, если книг будет много.
//
//    Реализуйте страницу автора, а также всю необходимую логику и структуру для работы этого раздела магазина,
//    которая включает в себя создание необходимого метода обработчика в соответствующем и уже существующем
//    или новом контроллере. Контроллер должен вызывать соответствующий сервис, чтобы сформировать ответ на
//    поступивший запрос, а сервис должен иметь доступ к необходимым разделам базы данных при
//    помощи репозиториев, которые обращаются к таблицам и получают обратно данные для дальнейшей обработки.
//
//    Советы и рекомендации
//    При выполнении задания руководствуйтесь документацией к проекту: техническим заданием, структурой данных,
//    Swagger API. Так как для работы с изображениями мы пока ещё не обладаем всем набором необходимых
//    инструментов, используйте «заглушки» изображений вроде тех, что предлагает mockaroo.com.

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }
}
