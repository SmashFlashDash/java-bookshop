package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.data.user.UserContact;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.lang.reflect.Field;
import java.util.*;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, OAuth2User {
    private final User bookstoreUser;

    public User getBookstoreUser() {
        return bookstoreUser;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public <A> A getAttribute(String name) {
        return null;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getAttributes() {
        // TODO: приватные поля парсить по методам
        Map<String, Object> map = new HashMap<>();
        Field[] fields = bookstoreUser.getClass().getFields();
        for (Field f : fields)
            map.put(f.getName(), f.get(bookstoreUser));
        return map;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return bookstoreUser.getPassword();
    }

    @Override
    public String getUsername() {
        return bookstoreUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
