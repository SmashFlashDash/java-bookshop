package com.example.MyBookShopApp.integration;

import com.example.MyBookShopApp.controllers.ChangeBookStatusController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChangeBookStatusControllerTests {
    private final ChangeBookStatusController controller;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleChangeBookStatus() {
    }

}