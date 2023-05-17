package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.token.BlackJwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListJwtRepository extends JpaRepository<BlackJwt, Long> {

    BlackJwt findByToken(String token);
}
