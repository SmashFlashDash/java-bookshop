package com.example.MyBookShopApp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    //  найти все книга по имени автора
    List<Book> findBooksByAuthor_FirstName(String name);

    //запрос при помощи аннотайи querry
    @Query("from Book")
    List<Book> customFindAllBooks();
}
