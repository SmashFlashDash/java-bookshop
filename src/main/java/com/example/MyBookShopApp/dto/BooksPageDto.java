package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.data.book.Book;
import lombok.Getter;

import java.util.List;

@Getter
public class BooksPageDto {
    private final Integer count;
    private final List<Book> books;

    public BooksPageDto(List<Book> books) {
        this.books = books;
        count = books.size();
    }
}
