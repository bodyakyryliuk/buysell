package com.example.buysell.service.registration;

import com.example.buysell.model.User;
import com.example.buysell.model.VerificationToken;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.security.authentication.OnRegistrationCompleteEvent;
import com.example.buysell.service.user.UserService;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;

    // return true if user exists
    @Override
    public boolean checkUserExists(String email){
        return userService.getUserByEmail(email) != null;
    }

    @Override
    public boolean checkVerificationToken(String token) {
        return userService.getVerificationToken(token) == null;
    }

    @Override
    public boolean checkTokenExpiryDate(String token) {
        Calendar calendar = Calendar.getInstance();
        // return true if token time expired
        return userService.getVerificationToken(token).getExpiryDate().getTime() - calendar.getTime().getTime() <= 0;
    }

    @Override
    public void processRegistration(WebUser webUser, HttpServletRequest request, ApplicationEventPublisher eventPublisher){
        User user = userService.save(webUser);
        Locale locale = request.getLocale();
        String appUrl = request.getContextPath();

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, locale, appUrl));
    }

    @Override
    public void confirmRegistration(String token, EmailAuthenticationProvider authenticationProvider) {
        User user = userService.getUserByToken(token);
        user.setActive(true);
        authenticationProvider.autoLogin(user.getEmail(), user.getPassword());
    }

}
