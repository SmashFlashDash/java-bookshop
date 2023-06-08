package com.example.MyBookShopApp.module.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource("/application-test.properties")
class BookReviewServiceTests {

    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
    }

    @Test
    void getReviewsByBook() {
    }
}