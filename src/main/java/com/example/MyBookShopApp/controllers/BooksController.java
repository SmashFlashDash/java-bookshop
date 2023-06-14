package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.repositories.BookRepository;
import com.example.MyBookShopApp.data.tag.TagDto;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagService;
import com.example.MyBookShopApp.dto.BookDto;
import com.example.MyBookShopApp.dto.BooksDtoPageDto;
import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.services.*;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
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
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final ResourceStorage storage;
    private final TagService tagService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookRatingService bookRating;
    private final BookReviewService bookReviewService;
    private final ModelMapper modelMapper;

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


    @GetMapping("/{slug}")
    public String bookPage(@PathVariable("slug") String slug, Principal principal, Model model) {
        Book book = bookRepository.findBookBySlug(slug);
        model.addAttribute("bookRating", bookRating.getBookRatingStars(book.getId()));
        model.addAttribute("reviews", bookReviewService.getReviewsByBook(book.getId()));
        model.addAttribute("slugBook", book);
        if (principal == null) {
            return "/books/slug";
        }
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
    public BooksDtoPageDto getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                                   @RequestParam("limit") Integer limit,
                                                   @CookieValue(value = "cartContents", required = false) String booksCart,
                                                   @CookieValue(value = "postContents", required = false) String booksPostponed,
                                                   Principal principal) {
        // TODO: использвоать modelMapper
        List<Book> books = bookService.getPageOfRecommendedBooks(offset, limit, principal, booksCart, booksPostponed).getContent();
        return new BooksDtoPageDto(books.stream().map(BookDto::new).collect(Collectors.toList()));
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
