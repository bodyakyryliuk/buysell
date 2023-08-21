package com.example.buysell.service;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.repository.UserRepository;
import com.example.buysell.user.WebUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void save(WebUser webUser) {
        User user = new User();

        // assign user details to the user object
        user.setFullName(webUser.getFullName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setEmail(webUser.getEmail());
        user.setActive(true);
        user.setAuthMethod("email");

        // give user default role of "employee"
        user.setRoles(List.of(new Role(user.getEmail(), "USER"), new Role(user.getEmail(), "MANAGER")));

        // save user in the database
        userRepository.save(user);
    }


    public void removeUser(Long id){
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}




