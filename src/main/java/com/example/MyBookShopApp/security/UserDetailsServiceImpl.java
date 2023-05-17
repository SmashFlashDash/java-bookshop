package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetailsImpl loadUserByUsername(String contact) throws UsernameNotFoundException {
        return userService.findUserByContact(contact)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found doh!"));
    }

//    @Override
//    public UserDetailsImpl loadUserByUsername(String emailOrPhone) throws UsernameNotFoundException {
//        return userContactRepository.findUserContactByContact(emailOrPhone)
//                .map(userContact -> new UserDetailsImpl(userContact.getUser()))
//                .orElseThrow(()->new UsernameNotFoundException("user not found doh!"));
//    }
}
