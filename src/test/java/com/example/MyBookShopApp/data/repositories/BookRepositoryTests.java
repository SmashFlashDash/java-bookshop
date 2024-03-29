package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.book.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class BookRepositoryTests {
    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTests(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Test
    @Transactional
    void getBestsellersTest() {
        List<Book> bestsellersList = bookRepository.getBestsellers();
        assertNotNull(bestsellersList);
        assertFalse(bestsellersList.isEmpty());
        assertThat(bestsellersList.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Transactional
    void findBooksByAuthorNameContainingTest() {
        String token = "Jase Petrozzi";
        List<Book> bookListByName = bookRepository.findBooksByAuthorNameContaining(token);
        assertNotNull(bookListByName);
        assertFalse(bookListByName.isEmpty());
        for (Book book : bookListByName) {
            // проверка что каждый элемент сожержит имя автора
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertThat(book.getAuthor().get(0).getName()).contains(token);
        }
    }

    @Test
    @Transactional
    void findBooksByTitleContainingTest() {
        String token = "Number1";
        List<Book> bookListByTitle = bookRepository.findBooksByTitleContaining(token);
        assertNotNull(bookListByTitle);
        assertFalse(bookListByTitle.isEmpty());
        for (Book book : bookListByTitle) {
            Logger.getLogger(this.getClass().getSimpleName()).info(book.toString());
            assertThat(book.getTitle()).contains(token);
        }
    }
}