package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.services.BookService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.ListIterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BookServiceTest {
    private final BookService bookService;

    @Test
    void getPageOfRecommendedBooks() {
        List<Book> books = bookService.getPageOfRecommendedBooks(0, 10, null, "/number1", "").getContent();
        assertEquals(books.size(), 2);
    }


    @Test
    void getPageOfPopularBooks() {
        List<Book> books = bookService.getPageOfPopularBooks(0, 9).getContent();
        ListIterator<Book> iter = books.listIterator();
        while (iter.hasNext()) {
            Book curBook = iter.next();
            if (iter.hasPrevious()) {
                Book prevBook = iter.previous();
                iter.next();
                double prev = popilarFormula(prevBook.getStatBought(), prevBook.getStatInCart(), prevBook.getStatPostponed());
                double cur = popilarFormula(curBook.getStatBought(), curBook.getStatInCart(), curBook.getStatPostponed());
                assertThat("popular", prev, greaterThanOrEqualTo(cur));
            }
        }
    }

    private double popilarFormula(int bought, int inCart, int postponed) {
        return bought + 0.7 * inCart + 0.4 * postponed;
    }
}
