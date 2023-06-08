package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.ListIterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource("/application-test.properties")
class BookServiceTest {
    private final BookService bookService;
    //  рассчитывающие популярность книги
    //  рейтинг отзыва на книгу расчитавает в рпози
    //  список рекомендуемых пользователю книг.

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getPageOfRecommendedBooks() {
        // зависит от куки
    }


    @Test
    void getPageOfPopularBooks() {
        List<Book> books = bookService.getPageOfPopularBooks(0, 9).getContent();
        ListIterator<Book> iter = books.listIterator();
        while (iter.hasNext()) {
            Book curBook = iter.next();
            if (iter.hasPrevious()) {
                Book prevBook = iter.previous();
                double prev = popilarFormula(prevBook.getStatBought(), prevBook.getStatInCart(), prevBook.getStatPostponed());
                double cur = popilarFormula(curBook.getStatBought(), curBook.getStatInCart(), curBook.getStatPostponed());
                assertThat("popular", cur, greaterThanOrEqualTo(cur));
            }
        }
    }

    private double popilarFormula(int bought, int inCart, int postponed) {
        return bought + 0.7 * inCart + 0.4 * postponed;
    }
}
