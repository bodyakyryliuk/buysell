package com.example.buysell.service.registration;

import com.example.buysell.model.User;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Calendar;

public interface RegistrationService {
    boolean checkUserExists(String email);

    boolean checkVerificationToken(String token);

    boolean checkTokenExpiryDate(String token);
    void processRegistration(WebUser webUser, HttpServletRequest request, ApplicationEventPublisher eventPublisher);

    void confirmRegistration(String token, EmailAuthenticationProvider authenticationProvider);

}
