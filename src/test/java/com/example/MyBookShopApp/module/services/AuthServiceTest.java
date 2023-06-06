package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ContextConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthServiceTest {
    private static RegistrationForm regForm;
    private static ContactConfirmationPayload payload;
    @MockBean
    private final UserService userServiceMock;
    @InjectMocks
    private final AuthService authService;


    @BeforeAll
    static void setUp() {
        regForm = new RegistrationForm();
        regForm.setEmail("test@mail.org");
        regForm.setName("Tester");
        regForm.setPass("iddqd");
        regForm.setPhone("903123232");
        payload = new ContactConfirmationPayload();
        payload.setCode("test-code");
        payload.setContact("test-contact");
    }

    @AfterAll
    static void tearDown() {
        regForm = null;
        payload = null;
    }

    @Test
    void registerNewOrGetUser_NotExits() {
        Mockito.doReturn(Optional.empty()).when(userServiceMock).findUserByContacts(any(List.class));
        User user = authService.registerNewOrGetUser(regForm);
        Mockito.verify(userServiceMock, Mockito.times(1)).saveNewUser(any(RegistrationForm.class));
    }

    @Test
    void registerNewOrGetUserExist() {
        Mockito.doReturn(Optional.of(new User())).when(userServiceMock).findUserByContacts(any(List.class));
        User user = authService.registerNewOrGetUser(regForm);
        Mockito.verify(userServiceMock, Mockito.times(0)).saveNewUser(any(RegistrationForm.class));
    }
}
