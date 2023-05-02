package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.security.UserDetailsServiceImpl;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserAuthService {

    private final UserRepository bookstoreUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserAuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService,
                           JWTUtil jwtUtil) {
        this.bookstoreUserRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public void registerNewUser(RegistrationForm registrationForm) {
        // TODO: если пользователя нет в БД если есть exception нужен
        if (bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()) == null) {
            User user = new User();
            user.setName(registrationForm.getName());
            user.setEmail(registrationForm.getEmail());
            user.setPhone(registrationForm.getPhone());
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
            user.setBalance(0);
            user.setRegTime(LocalDateTime.now());
            user.setHash(user.toString());
            bookstoreUserRepository.save(user);
        }
    }

    public ContactConfirmationResponse login(ContactConfirmationPayload payload) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        // BookstoreUserDetails userDetails = (BookstoreUserDetails) userDetailsService.loadUserByUsername(payload.getContact());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(userDetails);
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult(jwtToken);
        return response;
    }

    public Object getCurrentUser() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getBookstoreUser();
    }
}
