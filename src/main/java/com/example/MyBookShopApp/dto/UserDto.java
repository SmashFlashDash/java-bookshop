package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.data.user.ContactType;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.data.user.UserContact;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import lombok.Getter;

@Getter
public class UserDto {
    private final User user;
    private final String email;
    private final String phone;

    public UserDto(UserDetailsImpl userDetails) {
        user = userDetails.getBookstoreUser();
        email = user.getContacts().stream().filter(contact -> contact.getType() == ContactType.EMAIL).findFirst()
                .map(UserContact::getContact).orElse(null);
        phone = user.getContacts().stream().filter(contact -> contact.getType() == ContactType.PHONE).findFirst()
                .map(UserContact::getContact).orElse(null);
    }

    public String getName() {
        return user.getName();
    }
}
