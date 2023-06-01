package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@TestPropertySource("/application-test.properties")
public class AuthServiceSecurityTest {
    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @Test
    @WithUserDetails(value = "test-contact", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void jwtLogin() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String refToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setCode("test-password");
        payload.setContact("test-contact");

        ContactConfirmationResponse response = authService.jwtLogin(payload);
        assertEquals(refToken, response.getResult());
    }

    @Test
    @WithAnonymousUser
    void getCurrentUserAnonim() {
        assertTrue(authService.getCurrentUser().isEmpty());
    }

    @Test
    @WithUserDetails(value = "test-contact", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void getCurrentUser() {
        assertEquals("test-contact", authService.getCurrentUser().get().getUsername());
    }
}
