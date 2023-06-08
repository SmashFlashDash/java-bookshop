package com.example.MyBookShopApp.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    private String name;
    private String email;
    private String phone;
    private String pass;
    private String oauth;
    private String phoneCode;
    private String mailCode;
}
