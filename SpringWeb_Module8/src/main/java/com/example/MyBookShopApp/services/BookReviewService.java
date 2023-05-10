package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.repositories.RatingRepository;
import com.example.MyBookShopApp.data.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookReviewService {
    private final ReviewRepository reviewRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public BookReviewService(ReviewRepository reviewRepository, RatingRepository ratingRepository) {
        this.reviewRepository = reviewRepository;
        this.ratingRepository = ratingRepository;
    }

    // получить reviews отсортированными по date
    public List<BookReview> getReviewsByBook(Book book) {
        return reviewRepository.findAllByBookIdOrderByTimeDesc(book.getId());
    }

    @Transactional
    public void addNewReview(Integer bookId, String text, Integer userId) {
        BookReview review = new BookReview();
        review.setBookId(bookId);
        review.setText(text);
        review.setUserId(userId);
        reviewRepository.save(review);
    }
}
