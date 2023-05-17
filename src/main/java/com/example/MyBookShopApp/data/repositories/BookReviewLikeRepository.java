package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.book.review.BookReview;
import com.example.MyBookShopApp.data.book.review.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Integer> {
    Optional<BookReviewLike> findByUserIdAndReviewId(Integer userId, Integer reviewId);
}
