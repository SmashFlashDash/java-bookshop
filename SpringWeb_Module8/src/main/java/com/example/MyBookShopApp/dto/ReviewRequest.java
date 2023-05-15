package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Integer bookId;
    private String text;
}
