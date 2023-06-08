package com.example.MyBookShopApp;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class MyBookShopAppApplicationTests {
    private final MyBookShopAppApplication application;

    @Value("${auth.secret}")
    String authSecret;

    @Autowired
    public MyBookShopAppApplicationTests(MyBookShopAppApplication appApplication) {
        this.application = appApplication;
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(application);
    }

    @Test
    void verifyAuthSecret() {
        assertThat(authSecret, Matchers.containsString("box"));
    }

}
