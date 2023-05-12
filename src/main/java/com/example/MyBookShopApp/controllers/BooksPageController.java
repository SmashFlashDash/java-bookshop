package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.author.AuthorService;
import com.example.MyBookShopApp.data.book.BooksPageDto;
import com.example.MyBookShopApp.data.book.BookService;
import com.example.MyBookShopApp.data.genre.GenreService;
import com.example.MyBookShopApp.data.tag.TagDto;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.Date;

@Controller
public class BooksPageController {

    private final BookService bookService;
    private final TagService tagService;
    private final GenreService genreService;
    private final AuthorService authorService;
    @Autowired
    private BooksPageController(BookService bookService, TagService tagService, GenreService genreService, AuthorService authorService){
        this.bookService = bookService;
        this.tagService = tagService;
        this.genreService = genreService;
        this.authorService = authorService;
    }
//    @InitBinder
//    public void initBinder(@RequestParam("from") String from, @RequestParam("to") String to,  WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//
//    }

    @GetMapping("/books/recommended/page")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }
    @GetMapping(value = "/books/recent/page")
    @ResponseBody
    public BooksPageDto getNewBooksPage(
            @RequestParam(name = "from", required = false)
            @Pattern(regexp="\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d$", message="wrong format") @DateTimeFormat(pattern="dd.MM.yyyy")  Date from,
            @RequestParam(name = "to", required = false)
            @Pattern(regexp="\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d$", message="wrong format") @DateTimeFormat(pattern="dd.MM.yyyy")  Date to,
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit) {
        if (from == null && to == null){
            return new BooksPageDto(bookService.getPageOfNewBooks(offset, limit).getContent());
        } else if (from != null && to != null){
            return new BooksPageDto(bookService.getPageOfNewBooksDateBetween(from, to, offset, limit).getContent());
        } else if (from != null){
            return new BooksPageDto(bookService.getPageOfNewBooksDateFrom(from, offset, limit).getContent());
        } else {
            return new BooksPageDto(bookService.getPageOfNewBooksDateTo(to, offset, limit).getContent());
        }
    }
    @GetMapping("/books/popular/page")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }
    @GetMapping("/books/tags/page/{tagWord}")
    @ResponseBody
    public BooksPageDto getPageBooksTag(@PathVariable(value = "tagWord", required = false) TagDto tagWord,
                                        @RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        TagEntity tag = tagService.findByTagName(tagWord.getTag());
        return new BooksPageDto(bookService.getPageOfBooksByTag(tag, offset, limit).getContent());
    }
    @GetMapping("/books/genre/page/{slug}")
    @ResponseBody
    public BooksPageDto getGenreBooksPage(@PathVariable("slug") String slug,
                                          @RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPageOfBooksByListGenres(genreService.getGenreNodes(slug), offset, limit).getContent());
    }
    @GetMapping("/books/author/page/{slug}")
    @ResponseBody
    public BooksPageDto getAuthorBooksPage(@PathVariable("slug") String slug,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit){
        return new BooksPageDto(bookService.getPageOfBooksByAuthor(authorService.getAuthorBySlug(slug), offset, limit).getContent());
    }

    @GetMapping("/books/popular")
    public String getPopularBooks(Model model){
        model.addAttribute("active", "Popular");
        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 10).getContent());
        return "/books/popular";
    }
    @RequestMapping("/books/recent")
    public String getNewBooks(Model model){
        model.addAttribute("active", "Recent");
        // model.addAttribute("newBooks", bookService.getPageOfNewBooks(0, 10).getContent());
        // в script.min.js  $this.datepicker().data('datepicker') запрашивает книги за последний месяц
        // можно вычислть здесь Date и кидать в model и в script.min.js брать дату а не вычислять
        model.addAttribute("newBooks", bookService.getPageOfNewBooksDateBetween(
                new DateTime().minusMonths(1).toDate(), new Date(), 0, 10).getContent());
        return "/books/recent";
    }
    @GetMapping("/books/author/{slug}")
    public String getAuthorBooks(@PathVariable("slug") String slug, Model model){
        Author author = authorService.getAuthorBySlug(slug);
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.getPageOfBooksByAuthor(author, 0, 10).getContent());
        return "/books/author";
    }


}