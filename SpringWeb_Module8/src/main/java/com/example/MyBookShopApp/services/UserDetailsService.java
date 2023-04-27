package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository bookstoreUserRepository;

    @Autowired
    public UserDetailsService(UserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);
        if(bookstoreUser!=null){
            return new BookstoreUserDetails(bookstoreUser);
        }else{
            throw new UsernameNotFoundException("user not found doh!");
        }
    }
}
