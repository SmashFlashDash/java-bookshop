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

// TODO: в следующие адреса запрос приходит два раза второй в dto favicon.ico
//@GetMapping("/search/{searchWord}")   SearchPageController
//@GetMapping(value = {"/tags/{tagWord}"})  TagPageController
// потому что такая ссылка в каждом html
//<link href="favicon.ico" rel="shortcut icon">
// заменить href="favicon.ico" на th:href="@{favicon.ico}"  href="/favicon.ico"
// TODO: в bookService не лучший запрос через TagService и GenreService
//public Page<Book> getPageOfBooksByTag
//public Page<Book> getPageOfBooksByGenre
// как перенести метод в TagService чтобы получить tagEntity потом обратиться к полю
// tagEntity.books но возвращать список pageble
// как перенести метод в GenreService чтобы получить genreEntity потом обратиться к полю
// genreEntity.books но возвращать обьекты pageble
// TODO: GenreService лучше вынести триггер в БД при добаавлении в book2genre изменять поле countBooks в genreEntity
// вместо того чтобы кажыдй раз получать все книги чтобы посчитать кол-во книг для жанра
// public GenreDto getAllGenresDto()
// TODO: можно ли переносить обьект при переходе на другой EndPoint
// TODO: как отобразить tree структуру в thymeleaf
//public String postponedPage(Model model)
// TODO: правильно ли ставить JsonIgnore
// public List<TagEntity> tagsBooks()

//  Задание 2.4. Реализация списка книг по жанрам
//  Что нужно сделать
//      Для просмотра списка книг по жанрам на сайте предусмотрен раздел «Жанры», где пользователь выбирает
//  по иерархическому списку интересующий его жанр или поджанр и далее просматривает выбранный список книг.
//      Реализуйте необходимую логику и структуры для работы раздела «Жанры», включая вывод списка книг,
//  формированный по выбранному пользователем жанру. Также не забудьте о постраничном выводе контента в
//  этом разделе.
//      Советы и рекомендации
//      При выполнении задания руководствуйтесь документацией к проекту: техническим заданием,
//  структурой данных, Swagger API. Так как для работы с изображениями мы пока ещё не обладаем всем набором
//  необходимых инструментов, используйте «заглушки» изображений вроде тех, что предлагает mockaroo.com.
//  Дерево жанров технически представляет собой блок ссылок, а вложенность достигается путём
//  включения одних блоков и групп блоков в другие parent-блоки. Каждая ссылка в блоке ведёт на
//  страницу /genres/slug.html, в которой при помощи механизма пагинации осуществляется вывод книг,
//  обладающих выбранным из облака жанром. Для реализации этой задачи вам понадобится хранить
//  данные по жанрам (список жанров) и осуществлять их мэппинг с данными из таблицы books.
//  Далее, выполняя запрос на выборку книг с определённым жанром и анализируя результаты, вы
//  сможете определять контент — список книг из books для вывода на странице сайта при помощи
//  уже имеющегося в вашем распоряжении механизма пагинации контента. Рендеринг дерева жанров может
//  быть осуществлён средствами Thymeleaf или JQuery на ваш выбор.

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

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

//    // Страница genres
//    @GetMapping("/genres")
//    public String getGenres(Model model){
//        return "/genres/index";
//    }
//    // Страница authors
//    @GetMapping("/authors")
//    public String getAuthors(Model model){
//        return "/authors/index";
//    }

}
