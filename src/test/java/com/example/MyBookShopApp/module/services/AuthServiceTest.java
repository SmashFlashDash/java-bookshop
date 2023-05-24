package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthServiceTest {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Test
    void registerNewOrGetUser() {
        // RegistrationForm registrationForm
        RegistrationForm regForm = new RegistrationForm();
        regForm.setName("name");
        //regForm.setOauth("url");
        User user = authService.registerNewOrGetUser(regForm);
        // TODO: проверить что пользователь сейвистся в БД
        // что не сейвится если уже есть
        // что сейвится
        // там тестится сервис userService.saveNewUser(registrationForm)
        // надо ли делать его mock
    }

    @Test
    void login() {
        //  ContactConfirmationPayload payload
    }

    @Test
    void jwtLogin() {
        //  ContactConfirmationPayload payload
        ContactConfirmationPayload payload = new ContactConfirmationPayload();
        payload.setCode("test-code");
        payload.setContact("test-contact");
        ContactConfirmationResponse response = authService.jwtLogin(payload);
        String jwt = response.getResult();

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(), payload.getCode()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwtToken = jwtUtil.generateToken(userDetails);
        //  респонсе получаем jwtToken надо проверить его валидность
    }

    @Test
    void getCurrentUser() {
      // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      // if (auth instanceof AnonymousAuthenticationToken) {
      //   return Optional.empty();
      // }
      // return Optional.of((UserDetailsImpl) auth.getPrincipal());

      // типо проверить аномниманого и заранее залогиненного пользоваттлея
    }
}
