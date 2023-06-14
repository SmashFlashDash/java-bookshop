package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.dto.BookRatingStarsDto;
import com.example.MyBookShopApp.services.BookRatingService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookRatingServiceTests {
    private final BookRatingService bookRatingService;

    @Test
    void getBookRatingStars() {
        BookRatingStarsDto bookRatingStarsDto = bookRatingService.getBookRatingStars(1);
        assertEquals(bookRatingStarsDto.getFiveStar(), 2);
        assertEquals(bookRatingStarsDto.getOneStar(), 3);
    }
}