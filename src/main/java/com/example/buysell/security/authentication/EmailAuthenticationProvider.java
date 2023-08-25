package com.example.buysell.security.authentication;

import com.example.buysell.model.Role;
import com.example.buysell.model.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Slf4j
@RequiredArgsConstructor
@Component
public class EmailAuthenticationProvider implements AuthenticationProvider{
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder; // Use BCryptPasswordEncoder

    public void autoLogin(String email, String password){
        log.info("In auto login method. Email: " + email + ". Password: " + password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        log.info("In auto login method. User email: " + userDetails.getUsername() + ". Password: " + userDetails.getPassword() + ". Roles: " + userDetails.getAuthorities());

        // Create an Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        // Set the Authentication object to SecurityContextHolder
        SecurityContext sc = SecurityContextHolder.getContext();
        log.info("Before setting authentication: " + String.valueOf(sc.getAuthentication()));

        sc.setAuthentication(authentication);

        log.info("After setting authentication: " + String.valueOf(sc.getAuthentication()));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("in authenticate method");

        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (passwordMatches(password, userDetails.getPassword())) {
            log.info("Password matches");
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    // Helper method to compare passwords (you might use a more secure approach)
    private boolean passwordMatches(String enteredPassword, String password) {
        return passwordEncoder.matches(enteredPassword, password);
    }


}
