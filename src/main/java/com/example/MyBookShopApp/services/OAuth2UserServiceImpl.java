package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OAuth2UserServiceImpl implements OAuth2UserService {
    private final AuthService authService;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User defUser = delegate.loadUser(request);
        RegistrationForm regForm = new RegistrationForm();
        regForm.setName(defUser.getAttribute("name"));
        if ("github".equals(request.getClientRegistration().getRegistrationId())) {
            regForm.setOauth(defUser.getAttribute("url"));
        } else if ("facebook".equals(request.getClientRegistration().getRegistrationId())) {
            regForm.setOauth(defUser.getAttribute("url"));
        } else throw new BadCredentialsException("unknwn token");
        User user = authService.registerNewOrGetUser(regForm);
        return new UserDetailsImpl(user);
    }
}
