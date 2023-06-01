package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthorServiceTests {
    private final UserService userService;

    @Test
    void getAuthorBySlug() {
    }

    @Test
    void getAuthorsMap() {
    }
}
