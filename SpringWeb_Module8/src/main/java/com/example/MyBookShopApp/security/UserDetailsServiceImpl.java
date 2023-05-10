package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.repositories.UserContactRepository;
import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final UserService userService;

    @Override
    public UserDetailsImpl loadUserByUsername(String contact) throws UsernameNotFoundException {
        return userService.findUserByContact(contact)
                .map(UserDetailsImpl::new)
                .orElseThrow(()->new UsernameNotFoundException("user not found doh!"));
    }

//    @Override
//    public UserDetailsImpl loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
//        return userContactRepository.findUserContactByContact(emailOrPhone)
//                .map(userContact -> new UserDetailsImpl(userContact.getUser()))
//                .orElseThrow(()->new UsernameNotFoundException("user not found doh!"));
//    }
}
