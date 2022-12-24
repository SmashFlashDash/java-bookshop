package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.search.SearchWordDto;
import com.example.MyBookShopApp.data.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainPageController {
    private final BookService bookService;
    @Autowired
    public MainPageController(BookService bookService) {
        this.bookService = bookService;
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
        // TODO: правильно ли туда short передавать
        return bookService.getPageOfPopularBooks(0, 6).getContent();
    }
    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }
    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }


    @GetMapping("/")
    public String mainPage() {
        return "index";
    }



//    // Страница search
//    @GetMapping(value = {"/search", "/search/{searchWord}"})
//    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
//                                   Model model) {
//        model.addAttribute("searchWordDto", searchWordDto);
//        model.addAttribute("searchResults",
//                bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
//        return "/search/index";
//    }
//    @GetMapping("/search/page/{searchWord}")
//    @ResponseBody
//    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
//                                          @RequestParam("limit") Integer limit,
//                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
//        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
//    }

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
