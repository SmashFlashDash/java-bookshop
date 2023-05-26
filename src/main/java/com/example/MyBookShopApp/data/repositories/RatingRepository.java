package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.review.BookRating;
import com.example.MyBookShopApp.dto.BookRatingItem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<BookRating, Long> {

//    @Query("SELECT br.value, COUNT(br.value) FROM BookRating AS br WHERE br.bookId = :bookId GROUP BY br.value ORDER BY br.value ASC")
//    List<Object[]> getRatingsCount(@Param("bookId") Integer bookId);

    @Query("SELECT br.value AS value, COUNT(br.value) AS count FROM BookRating AS br WHERE br.bookId = :bookId GROUP BY br.value ORDER BY br.value ASC")
    List<BookRatingItem> getRatingsCount(@Param("bookId") Integer bookId);
}
