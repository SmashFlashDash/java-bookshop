package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.dto.BookRatingDto;
import com.example.MyBookShopApp.services.*;
import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.tag.TagDto;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ResourceStorage storage;
    private final TagService tagService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookRatingService bookRating;
    private final BookReviewService bookReviewService;

    // TODO:
    //  добавить в Book метод getDiscountPrice
    //  как добавить в fragment_header отображение кол-ва книг в корзине, и это должно выполняться на всех страницах
    //  не работает чтение куки в cart может связано с localhost, в /cart куки всегда null
    //  не очень решение @GetMapping("/favicon.ico") в BooksController
    //  не рабоатет крестик в оверлэй скачать книгу
    //  и скачка книг какойт exception
    //  -------
    //  Задание 1. Добавление книг в категорию «Отложенное»
    //  Что нужно сделать
    //  В вашем проекте пользователь может перемещать книги в категорию «Отложенное» перед покупкой или чтобы составить список чтения.
    //  Реализуйте функционал и необходимую логику раздела «Отложенное», учитывая, что его работа связана с такими процессами, как, например, расчёт
    //  рекомендаций для пользователей или определение популярности книги.
    //  ---------
    //  Задание 2. Механизм оценок и рейтингов книг
    //  Что нужно сделать
    //  Система рейтингов книг позволяет пользователям ориентироваться в контенте магазина, помогает упорядочить данные и сформировать рекомендации.
    //  Реализуйте функционал и необходимую логику данного раздела интернет-магазина.
    //  ----------
    //  Задание 3. Функционал отзывов на книгу
    //  Что нужно сделать
    //  Возможность оставить отзыв, рецензию, комментарий, сообщение, мнение о чём-либо, в том числе и о книге, — основа коммуникации людей в сети.
    //  Реализуйте функционал и необходимую логику данного раздела, чтобы пользователи магазина могли оставлять отзывы о приобретённой книге.
    //  Советы и рекомендации
    //  При выполнении задания руководствуйтесь документацией к проекту: техническим заданием, структурой данных.
    //  Обратите внимание, что пользовательский интерфейс в этом разделе будет меняться в зависимости от того, авторизован пользователь или нет.
    //  До тех пор, пока вы не познакомитесь с разделом безопасности и разграничения доступа к ресурсам интернет-магазина в следующих модулях, предполагается,
    //  что доступ к разделу отзывов есть у всех пользователей.


    @Autowired
    public BooksController(BookService bookService, BookRepository bookRepository, ResourceStorage storage,
                           TagService tagService, GenreService genreService, AuthorService authorService,
                           BookRatingService bookRating, BookReviewService bookReviewService) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.storage = storage;
        this.tagService = tagService;
        this.genreService = genreService;
        this.authorService = authorService;
        this.bookRating = bookRating;
        this.bookReviewService = bookReviewService;
    }


//    @GetMapping("/popular")
//    public String getPopularBooks(Model model) {
//        model.addAttribute("active", "Popular");
//        model.addAttribute("popularBooks", bookService.getPageOfPopularBooks(0, 10).getContent());
//        return "/books/popular";
//    }
    @GetMapping("/popular")
    public String popularPage(Model model) {
        model.addAttribute("active", "Popular");
        model.addAttribute("popularBooks", bookService.getBooksData());
        return "/books/popular";
    }
    @GetMapping("/popular/page")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
                                            @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }


//    @GetMapping("/recent")
//    public String recentPage(Model model) {
//        model.addAttribute("recentBooks", bookService.getBooksData());
//        return "/books/recent";
//    }
    @RequestMapping("/recent")
    public String getNewBooks(Model model) {
        model.addAttribute("active", "Recent");
        model.addAttribute("newBooks", bookService.getPageOfNewBooksDateBetween(
                new DateTime().minusMonths(1).toDate(), new Date(), 0, 10).getContent());
        return "/books/recent";
    }
    @GetMapping(value = "/recent/page")
    @ResponseBody
    public BooksPageDto getNewBooksPage(
            @RequestParam(name = "from", required = false)
            @Pattern(regexp = "\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d$", message = "wrong format") @DateTimeFormat(pattern = "dd.MM.yyyy") Date from,
            @RequestParam(name = "to", required = false)
            @Pattern(regexp = "\\d\\d\\.\\d\\d\\.\\d\\d\\d\\d$", message = "wrong format") @DateTimeFormat(pattern = "dd.MM.yyyy") Date to,
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit) {
        if (from == null && to == null) {
            return new BooksPageDto(bookService.getPageOfNewBooks(offset, limit).getContent());
        } else if (from != null && to != null) {
            return new BooksPageDto(bookService.getPageOfNewBooksDateBetween(from, to, offset, limit).getContent());
        } else if (from != null) {
            return new BooksPageDto(bookService.getPageOfNewBooksDateFrom(from, offset, limit).getContent());
        } else {
            return new BooksPageDto(bookService.getPageOfNewBooksDateTo(to, offset, limit).getContent());
        }
    }

    // TODO: считаем всех пользвотелей авторизованными и старница slugmy
    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Model model) {
        Book book = bookRepository.findBookBySlug(slug);
        BookRatingDto bookRatingDto = bookRating.getBookRating(book.getId());
        List<BookReview> reviews = bookReviewService.getReviewsByBook(book);
        model.addAttribute("bookRating", bookRatingDto);
        model.addAttribute("reviews", reviews);
        model.addAttribute("slugBook", book);
        return "/books/slugmy";
    }
    @PostMapping("/{slug}/img/save")
    public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug) throws IOException {
        String savePath = storage.saveNewBookImage(file, slug);
        Book bookToUpdate = bookRepository.findBookBySlug(slug);
        bookToUpdate.setImage(savePath);
        bookRepository.save(bookToUpdate); //save new path in db here

        return "redirect:/books/" + slug;
    }


    @GetMapping("/download/{hash}")
    public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException {
        Path path = storage.getBookFilePath(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);

        MediaType mediaType = storage.getBookFileMime(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);

        byte[] data = storage.getBookFileByteArray(hash);
        Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
                .contentType(mediaType)
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/recommended/page")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }


    @GetMapping("/tags/page/{tagWord}")
    @ResponseBody
    public BooksPageDto getPageBooksTag(@PathVariable(value = "tagWord", required = false) TagDto tagWord,
                                        @RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        TagEntity tag = tagService.findByTagName(tagWord.getTag());
        return new BooksPageDto(bookService.getPageOfBooksByTag(tag, offset, limit).getContent());
    }

    @GetMapping("/genre/page/{slug}")
    @ResponseBody
    public BooksPageDto getGenreBooksPage(@PathVariable("slug") String slug,
                                          @RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByListGenres(genreService.getGenreNodes(slug), offset, limit).getContent());
    }

    @GetMapping("/author/{slug}")
    public String getAuthorBooks(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorBySlug(slug);
        model.addAttribute("author", author);
        model.addAttribute("books", bookService.getPageOfBooksByAuthor(author, 0, 10).getContent());
        return "/books/author";
    }

    @GetMapping("/author/page/{slug}")
    @ResponseBody
    public BooksPageDto getAuthorBooksPage(@PathVariable("slug") String slug,
                                           @RequestParam("offset") Integer offset,
                                           @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfBooksByAuthor(authorService.getAuthorBySlug(slug), offset, limit).getContent());
    }


}
