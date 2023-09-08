package com.example.buysell.security.authentication;

import com.example.buysell.service.registration.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Transactional
@RequiredArgsConstructor
public class GitHubSuccessHandler implements AuthenticationSuccessHandler {
    private final RegistrationService registrationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println(authentication.getPrincipal());
        if (registrationService.createOAuth2Token(authentication, response))
            response.sendRedirect("/");
        else {
            response.sendRedirect("/login");
        }
    }
}
