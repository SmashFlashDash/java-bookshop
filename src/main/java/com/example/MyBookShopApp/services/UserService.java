package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.repositories.UserContactRepository;
import com.example.MyBookShopApp.data.repositories.UserRepository;
import com.example.MyBookShopApp.data.user.ContactType;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.data.user.UserContact;
import com.example.MyBookShopApp.dto.ProfileUpdateRequest;
import com.example.MyBookShopApp.dto.ProfileUpdateResponse;
import com.example.MyBookShopApp.dto.RegistrationForm;
import com.example.MyBookShopApp.security.UserDetailsImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserContactRepository userContactRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(@Lazy AuthService authService, UserRepository userRepository, UserContactRepository userContactRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userContactRepository = userContactRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findUserByContact(String contact) {
        return userContactRepository.findUserContactByContact(contact).map(UserContact::getUser);
    }

    public Optional<User> findUserByContacts(List<String> contacts) {
        return userContactRepository.findUserContactByContactIn(contacts).map(UserContact::getUser);
    }

    public User saveNewUser(RegistrationForm registrationForm) {
        User user = createUser(registrationForm.getName());
        List<UserContact> contacts = user.getContacts();
        if (registrationForm.getPass() != null) {
            user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
        }
        if (registrationForm.getEmail() != null) {
            contacts.add(createUserContact(user, ContactType.EMAIL, registrationForm.getEmail()));
        }
        if (registrationForm.getPhone() != null) {
            contacts.add(createUserContact(user, ContactType.PHONE, registrationForm.getPhone()));
        }
        if (registrationForm.getOauth() != null) {
            if (registrationForm.getOauth().contains("github"))
                contacts.add(createUserContact(user, ContactType.OAuth_GitHub_URL, registrationForm.getOauth()));
            else if (registrationForm.getOauth().contains("facebook"))
                contacts.add(createUserContact(user, ContactType.OAuth_Facebook_URL, registrationForm.getOauth()));
        }
        return userRepository.save(user);
    }

    public ProfileUpdateResponse updateUser(ProfileUpdateRequest upd) {
        User user = authService.getCurrentUser().get().getBookstoreUser();
        if (!upd.getName().isEmpty()) user.setName(upd.getName());
        if (!upd.getPassword().isEmpty() & upd.getPassword().equals(upd.getPasswordReply()))
            user.setPassword(passwordEncoder.encode(upd.getPassword()));
        if (!upd.getMail().isEmpty()) updateOrCreateContact(user, ContactType.EMAIL, upd.getMail());
        if (!upd.getPhone().isEmpty()) updateOrCreateContact(user, ContactType.PHONE, upd.getPhone());

        ProfileUpdateResponse response = new ProfileUpdateResponse();
        try {
            response.setUser(new UserDetailsImpl(userRepository.save(user)));
        } catch (DataIntegrityViolationException e) {
            response.setUser(new UserDetailsImpl(userRepository.findById(user.getId()).get()));
            response.setError(e.getMessage());
        }
        return response;
    }

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setBalance(0);
        user.setRegTime(LocalDateTime.now());
        user.setHash(passwordEncoder.encode(String.valueOf(user.hashCode())));
        return user;
    }

    private UserContact createUserContact(User user, ContactType type, String contact) {
        UserContact userContact = new UserContact();
        userContact.setUser(user);
        userContact.setType(type);
        userContact.setApproved((short) 0);
        userContact.setCode("0");
        userContact.setCodeTrails(0);
        userContact.setCodeTime(LocalDateTime.now());
        userContact.setContact(contact);
        return userContact;
    }

    private void updateOrCreateContact(User user, ContactType type, String newContact) {
        user.getContacts().stream().filter(contact -> contact.getType() == type).findFirst()
                .map(contact -> {
                    contact.setContact(newContact);
                    return contact;
                })
                .orElseGet(() -> {
                    UserContact contact = createUserContact(user, type, newContact);
                    user.getContacts().add(contact);
                    return contact;
                });
    }
}
