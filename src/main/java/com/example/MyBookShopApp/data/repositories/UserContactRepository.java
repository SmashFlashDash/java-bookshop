package com.example.MyBookShopApp.data.repositories;

import com.example.MyBookShopApp.data.user.UserContact;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, Integer> {
    Optional<UserContact> findUserContactByContact(String contact);
    Optional<UserContact> findUserContactByContactIn(List<String> contacts);
}
