package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.book.review.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<BookReview, Integer> {

    List<BookReview> findAllByBookIdOrderByTimeDesc(Integer bookId);
}
