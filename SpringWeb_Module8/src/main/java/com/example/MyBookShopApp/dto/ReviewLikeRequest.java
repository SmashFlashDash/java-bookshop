package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class ReviewLikeRequest {
    private final Integer reviewid;
    private final Short value;
}
