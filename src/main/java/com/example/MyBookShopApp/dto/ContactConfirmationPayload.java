package com.example.MyBookShopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ContactConfirmationPayload {
    private String contact;
    private String code;
}
