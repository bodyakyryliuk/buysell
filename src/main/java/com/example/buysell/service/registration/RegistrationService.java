package com.example.buysell.service.registration;

import com.example.buysell.model.User;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface RegistrationService {
    boolean checkUserExists(String email);

    boolean checkVerificationToken(String token);

    boolean checkTokenExpiryDate(String token);

    void processRegistration(WebUser webUser, HttpServletRequest request, ApplicationEventPublisher eventPublisher);

    void confirmRegistration(String token, EmailAuthenticationProvider authenticationProvider);

    User saveUserFromOAuth2User(OAuth2User oAuth2User);

    boolean createOAuth2Token(Authentication authentication, HttpServletResponse response);

}
