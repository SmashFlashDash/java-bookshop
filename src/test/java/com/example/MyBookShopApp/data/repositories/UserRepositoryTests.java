package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTests {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void testAddNewUser() {
        User user = new User();
        user.setPassword("");
        user.setBalance(0);
        user.setName("Name");
        user.setRegTime(LocalDateTime.now());
        user.setHash(String.valueOf(user.hashCode()));
        assertNotNull(userRepository.save(user));
    }
}
