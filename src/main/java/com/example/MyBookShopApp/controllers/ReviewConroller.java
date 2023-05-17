package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.dto.BookStatusDto;
import com.example.MyBookShopApp.dto.ReviewLikeRequest;
import com.example.MyBookShopApp.dto.ReviewRequest;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.services.BookReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
public class ReviewConroller {
    private final BookReviewService bookReviewService;

    @Autowired
    public ReviewConroller(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @PostMapping(value = "/bookReview")
    public ResponseEntity<BookStatusDto> addReview(@RequestBody ReviewRequest review, HttpServletRequest request) {
        bookReviewService.addNewReview(review.getBookId(), review.getText());
        return ResponseEntity.ok(new BookStatusDto(true));
    }

    @PostMapping(value = "/rateBookReview")
    public ResponseEntity<BookStatusDto> likeReview(@RequestBody ReviewLikeRequest like, HttpServletRequest request) {
        boolean result = bookReviewService.addLike(like.getReviewid(), like.getValue());
        return ResponseEntity.ok(new BookStatusDto(result));
    }
}
