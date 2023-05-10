package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
public class ProfileUpdateResponse {
    private UserDetailsImpl user = null;
    private String error = null;
}
