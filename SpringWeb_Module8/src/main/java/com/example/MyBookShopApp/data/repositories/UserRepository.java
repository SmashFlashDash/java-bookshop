package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUsersByName(String name);
    Optional<User> findUserByHash(String hash);
}
