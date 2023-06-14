package com.example.MyBookShopApp.integration;

import com.example.MyBookShopApp.controllers.AuthController;
import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthControllerTests {
    private final AuthController controller;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final AuthService authService;

    // регистрацию новой учётной записи
    // варианты login (по номеру телефона и по почте)
    // logout

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registration() throws Exception {
        mockMvc.perform(post("/reg")
                        .param("email", "testReg@test.test")
                        .param("name", "Tester")
                        .param("pass", "password")
                        .param("phone", "+7 000000000")
                        .param("oauth", "")
                        .param("phoneCode", "")
                        .param("mailCode", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    void loginByPhone() throws Exception {
        ContactConfirmationPayload payload = ContactConfirmationPayload.builder()
                .code("222 222")
                .contact("+7 (222) 222 2222").build();
        String jsoon = objectMapper.writeValueAsString(payload);
        String token = authService.jwtLogin(payload).getResult();
        mockMvc.perform(post("/login")
                        .content(jsoon).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().value("token", token));
    }

    @Test
    void loginByEmail() throws Exception {
        ContactConfirmationPayload payload = ContactConfirmationPayload.builder()
                .code("222 222")
                .contact("test@test.test").build();
        String jsoon = objectMapper.writeValueAsString(payload);
        String token = authService.jwtLogin(payload).getResult();
        mockMvc.perform(post("/login")
                        .content(jsoon).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().value("token", token));
    }

}