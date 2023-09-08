package com.example.buysell.security.authentication;

import com.example.buysell.model.User;
import com.example.buysell.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final UserServiceImpl userService;
    private final MessageSource messageSource;
    private final JavaMailSender javaMailSender;
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);


        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String url = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = "Confirm your registration in BuySell application";


        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + url);
        javaMailSender.send(email);
    }

}
