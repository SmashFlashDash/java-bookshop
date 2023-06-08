package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.security.LogoutHandlerImpl;
import com.example.MyBookShopApp.security.UserDetailsServiceImpl;
import com.example.MyBookShopApp.security.jwt.JWTRequestFilter;
import com.example.MyBookShopApp.services.OAuth2UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final LogoutHandlerImpl logoutHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTRequestFilter jwtFilter;
    private final OAuth2UserServiceImpl oAuth2UserService;

    public SecurityConfig(LogoutHandlerImpl logoutHandler,
                          @Lazy JWTRequestFilter jwtFilter,
                          @Lazy UserDetailsServiceImpl userDetailsService,
                          @Lazy OAuth2UserServiceImpl oAuth2UserService) {
        this.logoutHandler = logoutHandler;
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/my", "/profile").authenticated()//.hasRole("USER")
                .antMatchers("/**").permitAll()
                .and().formLogin()
                .loginPage("/signin").failureUrl("/signin")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/signin").deleteCookies("token")
                .addLogoutHandler(logoutHandler)    // jwt blackilist
                .and().oauth2Login().defaultSuccessUrl("/my")
                .userInfoEndpoint()
                .oidcUserService(this.oidcUserService())    // regUser
                .userService(oAuth2UserService::loadUser)   // regUser
                .and().and().oauth2Client();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();
        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            return oidcUser;
        };
    }
}

