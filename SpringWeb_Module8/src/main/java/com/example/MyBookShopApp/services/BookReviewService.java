package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.repositories.RatingRepository;
import com.example.MyBookShopApp.data.repositories.ReviewRepository;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReviewService {
    private final AuthService authService;
    private final ReviewRepository reviewRepository;

    public List<BookReview> getReviewsByBook(Integer bookId) {
        return reviewRepository.findAllByBookIdOrderByTimeDesc(bookId);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public void addNewReview(Integer bookId, String text) {
        Integer userId = authService.getCurrentUser().get().getBookstoreUser().getId();
        BookReview review = new BookReview();
        review.setBookId(bookId);
        review.setText(text);
        review.setUserId(userId);
        reviewRepository.save(review);
    }
}
