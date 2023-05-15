package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.book.review.BookRating;
import com.example.MyBookShopApp.data.repositories.RatingRepository;
import com.example.MyBookShopApp.dto.BookRatingItem;
import com.example.MyBookShopApp.dto.BookRatingStarsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookRatingService {
    private final RatingRepository bookRating;

    @Autowired
    public BookRatingService(RatingRepository bookRating) {
        this.bookRating = bookRating;
    }

    @PreAuthorize("isAuthenticated()")
    public void guestSetRaitingToBokk(Integer bookId, Short value) {
        BookRating rating = new BookRating();
        rating.setBookId(bookId);
        rating.setValue(value);
        bookRating.save(rating);
    }

    public BookRatingStarsDto getBookRatingStars(Integer id) {
        List<BookRatingItem> listValues = bookRating.getRatingsCount(id);
        return new BookRatingStarsDto(listValues);
    }
}
