package com.example.MyBookShopApp.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String name = null;
    private String mail = null;
    private String phone = null;
    private String password = null;
    private String passwordReply = null;
}
