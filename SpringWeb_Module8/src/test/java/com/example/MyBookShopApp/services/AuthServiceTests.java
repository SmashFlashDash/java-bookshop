package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.RegistrationForm;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTests {
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private RegistrationForm registrationForm;

    @MockBean
    private UserRepository userRepositoryMock;

    @Autowired
    public AuthServiceTests(PasswordEncoder passwordEncoder, AuthService authService) {
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @BeforeEach
    void setUp() {
        registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@mail.org");
        registrationForm.setName("Tester");
        registrationForm.setPass("iddqd");
        registrationForm.setPhone("903123232");
    }

    @AfterEach
    void tearDown() {
        registrationForm = null;
    }

    @Test
    void registerNewOrGetUser() {
        // TODO: то не unit test т.к. в auth Service еще используется зависимость userService
        //  мб над использовать @InjectMock и @Mock
//        User user = authService.registerNewOrGetUser(registrationForm);
//        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(Mockito.any(User.class));
//
//        assertEquals(user.getName(), "Tester");
//        assertTrue(passwordEncoder.matches(registrationForm.getPass(), user.getPassword()));
//        assertTrue(CoreMatchers.is(user.getContacts().size()).matches(2));
        // проверка что userRepository вызвал save на обьект User был вызыван

    }
}