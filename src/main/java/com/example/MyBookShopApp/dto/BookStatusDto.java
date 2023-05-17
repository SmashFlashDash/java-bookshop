package com.example.MyBookShopApp.dto;


public class BookStatusDto {
    boolean result;

    public BookStatusDto(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
