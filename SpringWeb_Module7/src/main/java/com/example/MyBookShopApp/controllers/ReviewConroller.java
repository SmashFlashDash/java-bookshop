package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.dto.BookStatusDto;
import com.example.MyBookShopApp.services.BookReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ReviewConroller {
    private final BookReviewService bookReviewService;

    @Autowired
    public ReviewConroller(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @PostMapping("/bookReview")
    public ResponseEntity<BookStatusDto> addReview(@CookieValue(name = "postContents", required = false) Cookie postContents,
                                                      @CookieValue(name = "cartContents", required = false) Cookie cartContents,
                                                      HttpServletRequest request, HttpServletResponse response, Model model) {
        String text = request.getParameter("text");
        Integer bookId = Integer.parseInt(request.getParameter("bookId"));
        bookReviewService.addNewReview(bookId, text, 1);    // TODO: 2 заглушка
        return ResponseEntity.ok(new BookStatusDto(true));
    }
}
