package com.example.MyBookShopApp.integration;

import com.example.MyBookShopApp.controllers.ChangeBookStatusController;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChangeBookStatusControllerTests {
    private final MockMvc mockMvc;
    private final ChangeBookStatusController controller;
    private final ObjectMapper objectMapper;
    private Cookie cookie;

    @BeforeEach
    void setUp() {
        cookie = new Cookie("cartContents", "/number1/number2");
    }

    @AfterEach
    void tearDown() {
        cookie = null;
    }

    @Test
    void addBookToCart() throws Exception {
        Map<String, String> body = new HashMap<>() {{
            put("status", "CART");
        }};
        String jsoon = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/books/changeBookStatus/{slug}", "snowriders")
                        .cookie(cookie)
                        .content(jsoon)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().value(cookie.getName(), cookie.getValue() + "/snowriders"));
    }

    @Test
    void removeBookFromCart() throws Exception {
        Map<String, String> body = new HashMap<>() {{
            put("status", "UNLINK");
        }};
        String jsoon = objectMapper.writeValueAsString(body);
        mockMvc.perform(post("/books/changeBookStatus/{slug}", "number2")
                        .cookie(cookie)
                        .content(jsoon)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().value(cookie.getName(), cookie.getValue().replace("/number2", "")));
    }

}