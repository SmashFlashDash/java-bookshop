package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.review.BookRating;
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

    // TODO: доделать метод
    @Transactional
    public void addNewReview(Integer bookId, String text, Short ratingValue, Integer userId) {
        BookRating rating = new BookRating();
        rating.setValue(ratingValue);
        rating.setBookId(bookId);
        ratingRepository.save(rating);
        BookReview review = new BookReview();
        review.setBookId(bookId);
        review.setText(text);
        review.setUserId(userId);
        review.setBookRating(rating);
        reviewRepository.save(review);
    }
}
