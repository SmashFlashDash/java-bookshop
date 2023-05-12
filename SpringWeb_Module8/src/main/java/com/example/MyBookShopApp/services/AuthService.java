package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Transactional
    public User registerNewOrGetUser(RegistrationForm registrationForm) {
        String[] contacts = {registrationForm.getPhone(), registrationForm.getEmail(), registrationForm.getOauth()};
        List<String> contactsClear = Arrays.stream(contacts).filter(Objects::nonNull).collect(Collectors.toList());
        return userService.findUserByContacts(contactsClear).orElseGet(() -> userService.saveNewUser(registrationForm));
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    //  TODO: ошибка если в User, UserContact FetchType.Lazy
    // org.hibernate.LazyInitializationException: could not initialize proxy [com.example.MyBookShopApp.data.user.User#101] - no Session
    //	at org.hibernate.proxy.AbstractLazyInitializer.initialize(AbstractLazyInitializer.java:170) ~[hibernate-core-5.4.32.Final.jar:5.4.32.Final]
    //	at com.example.MyBookShopApp.security.UserDetailsImpl.getUsername(UserDetailsImpl.java:60) ~[classes/:na]
    //	at com.example.MyBookShopApp.security.jwt.JWTUtil.validateToken(JWTUtil.java:59) ~[classes/:na]
    //	at com.example.MyBookShopApp.security.jwt.JWTRequestFilter.doFilterInternal(JWTRequestFilter.java:46) ~[classes/:na]l
    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public Optional<UserDetailsImpl> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of((UserDetailsImpl) auth.getPrincipal());
    }
}
