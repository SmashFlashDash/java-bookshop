package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class ChangeBookStatusRequest {
    private Integer bookId;
    private Short value;
}
