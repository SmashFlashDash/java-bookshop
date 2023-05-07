package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.security.LogoutHandlerImpl;
import com.example.MyBookShopApp.security.UserDetailsServiceImpl;
import com.example.MyBookShopApp.security.jwt.JWTRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final LogoutHandlerImpl logoutHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTRequestFilter jwtFilter;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }



    // нужен только если конфигурируть через filterChain а не configre
    // но созадавать в другом конфиге чтобы здесь имплементить
    // вместо configure(AuthenticationManagerBuilder auth)
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(getPasswordEncoder());
//        return authenticationProvider;
//    }
    // можно было клас User implement userDetails и тогда не нужно кастовать
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return s -> (UserDetails) userRepository.findBookstoreUserByEmail(s).orElseThrow(() ->new UsernameNotFoundException(""));
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/**").permitAll()
//                .antMatchers("/my", "/profile").authenticated().and()
//                .formLogin().loginPage("/signin").failureUrl("/signin").and()
//                .logout().logoutUrl("/logout").logoutSuccessUrl("/signin").deleteCookies("token")
//                .addLogoutHandler(logoutHandler).and()
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
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
                        .oidcUserService(this.oidcUserService()) // regUser
                        .userService(this.oAuth2UserService()) // regUser
                .and().and().oauth2Client();
//                .and().exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

        // TODO: для oauth нужно включить сессии, заккоментить отключение
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();// breakpoint here
        return new OAuth2UserService() {
            @SneakyThrows
            @Override
            public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
                OAuth2User defUser = delegate.loadUser(oAuth2UserRequest);
                Authentication authentication = authenticationManagerBean()
                        .authenticate(new UsernamePasswordAuthenticationToken("w@w.w", "222 222"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return defUser;
            }
        };
//        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();// breakpoint here
//        return request -> {
//            OAuth2User user = delegate.loadUser(request); //and breakpoint here
//            if (!"github".equals(request.getClientRegistration().getRegistrationId())) {
//                return user;
//            }
//
//            OAuth2AuthorizedClient client = new OAuth2AuthorizedClient
//                    (request.getClientRegistration(), user.getName(), request.getAccessToken());
//            String url = user.getAttribute("organizations_url");
//            List<Map<String, Object>> orgs = rest
//                    .get().uri(url)
//                    .attributes(oauth2AuthorizedClient(client))
//                    .retrieve()
//                    .bodyToMono(List.class)
//                    .block();
//
//            if (orgs.stream().anyMatch(org -> "spring-projects".equals(org.get("login")))) {
//                return user;
//            }
//
//            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Not in Spring Team", ""));
//        };
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();
        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            OidcUser oidcUser = delegate.loadUser(userRequest);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            // TODO
            // 1) Fetch the authority information from the protected resource using accessToken
            // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
            // 3) Create a copy of oidcUser but use the mappedAuthorities instead
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
            return oidcUser;
        };
    }
}

