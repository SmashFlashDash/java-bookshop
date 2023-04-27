package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findBookstoreUserByEmail(String email);
}
