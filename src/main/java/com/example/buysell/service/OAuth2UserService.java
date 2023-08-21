//package com.example.buysell.service;
//
//import com.example.buysell.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.user.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//@Service
//@RequiredArgsConstructor
//public class OAuth2UserService {
//    private final UserRepository userRepository;
//
//    public void saveUser(DefaultOAuth2User oauth2User) throws OAuth2AuthenticationException {
//
//        // List<GrantedAuthority> authorities = // Determine the roles for the user based on your application logic
//
//        Map<String, Object> attributes = oauth2User.getAttributes();
//        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));
//        // Add the roles as authorities
//        OAuth2User oAuth2User = new DefaultOAuth2User(authorities, attributes, "email");
//        S save = userRepository.save(oAuth2User);
//    }
//
//}
