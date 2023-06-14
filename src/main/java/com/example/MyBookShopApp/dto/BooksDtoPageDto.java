package com.example.MyBookShopApp.dto;

import lombok.Getter;

import java.util.List;


@Getter
public class BooksDtoPageDto {
    private final List<BookDto> books;
    private final Integer count;

    public BooksDtoPageDto(List<BookDto> books) {
        this.books = books;
        this.count = books.size();
    }
}
