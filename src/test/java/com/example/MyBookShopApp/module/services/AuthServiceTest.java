package com.example.MyBookShopApp.module.services;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.ContactConfirmationPayload;
import com.example.MyBookShopApp.dto.ContactConfirmationResponse;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.services.AuthService;
import com.example.MyBookShopApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthServiceTest {
    private static RegistrationForm regForm;
    private static ContactConfirmationPayload payload;
    private static AuthenticationImpl authImpl;
    private static UserDetailsImpl userDetails;
    @MockBean
    private final UserService userServiceMock;
    @MockBean
    private final AuthenticationManager authenticationManagerMock;
    @InjectMocks
    private final AuthService authService;
    private final JWTUtil jwtUtil;


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
        User user = new User();
        userDetails = new UserDetailsImpl(user);
        authImpl = new AuthenticationImpl(userDetails);
    }

    @AfterAll
    static void tearDown() {
        regForm = null;
        payload = null;
        userDetails = null;
        authImpl = null;
    }

    @Test
    void registerNewOrGetUser_True() {
        Mockito.doReturn(Optional.empty()).when(userServiceMock).findUserByContacts(any(List.class));
        User user = authService.registerNewOrGetUser(regForm);
        Mockito.verify(userServiceMock, Mockito.times(1)).saveNewUser(any(RegistrationForm.class));
    }

    @Test
    void registerNewOrGetUser_False() {
        Mockito.doReturn(Optional.of(new User())).when(userServiceMock).findUserByContacts(any(List.class));
        User user = authService.registerNewOrGetUser(regForm);
        Mockito.verify(userServiceMock, Mockito.times(0)).saveNewUser(any(RegistrationForm.class));
    }

    @Test
    void login() {
        //  ContactConfirmationPayload payload
    }

    @Test
    void jwtLogin() {
        String refToken = jwtUtil.generateToken(userDetails);
        Mockito.doReturn(authImpl).when(authenticationManagerMock).authenticate(any(UsernamePasswordAuthenticationToken.class));
        ContactConfirmationResponse response = authService.jwtLogin(payload);
        assertEquals(refToken, response.getResult());
    }

    @Test
    void getCurrentUser() {
      assertTrue(authService.getCurrentUser().isEmpty());
      // TODO: проверить залогиненного пользоваттлея
      //  но как залогинить ели все на моках
      // SecurityContext.setContext(реализ интерфейса)
      // authService.jwtLogin(payload);
      // assertTrue(authService.getCurrentUser().isPresent());
    }
}


class AuthenticationImpl implements Authentication {
    private final UserDetailsImpl userDetails;

    public AuthenticationImpl(UserDetailsImpl userDetails) {
        this.userDetails = userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return null;
    }
}
