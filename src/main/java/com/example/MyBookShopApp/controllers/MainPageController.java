package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.search.SearchWordDto;
import com.example.MyBookShopApp.data.tag.TagEntity;
import com.example.MyBookShopApp.data.tag.TagService;
import com.example.MyBookShopApp.dto.BooksPageDto;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainPageController {
    private final BookService bookService;
    private final TagService tagService;
    private final AuthService authService;

    @ModelAttribute("active")
    public String activePage() {
        return "Main";
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks(@CookieValue(value = "cartContents", required = false) String booksCart,
                                       @CookieValue(value = "postContents", required = false) String booksPostponed,
                                       Principal principal) {
        return bookService.getPageOfRecommendedBooks(0, 6, principal, booksCart, booksPostponed).getContent();
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
    public List<TagEntity> tagsBooks() {
        return tagService.findAllSortedByBooksCount();
    }

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                   Model model) throws EmptySearchException {
        if (searchWordDto == null) {
            throw new EmptySearchException("Поиск по null невозможен");
        }
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("searchResults", bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
        return "/search/index";
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
    }
}
