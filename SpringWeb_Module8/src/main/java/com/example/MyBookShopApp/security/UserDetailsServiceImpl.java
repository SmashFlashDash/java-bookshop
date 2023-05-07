package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.data.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String s) throws UsernameNotFoundException {
        User bookstoreUser = userRepository.findUserByEmail(s);
        if(bookstoreUser!=null){
            return new UserDetailsImpl(bookstoreUser);
        }else{
            throw new UsernameNotFoundException("user not found doh!");
        }
    }
}
