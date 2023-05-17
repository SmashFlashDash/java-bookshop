package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.security.UserDetailsImpl;
import lombok.Data;

@Data
public class ProfileUpdateResponse {
    private UserDetailsImpl user = null;
    private String error = null;
}
