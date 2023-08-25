package com.example.buysell.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class EmailSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Determine the appropriate redirection URL based on the context
        String redirectUrl = determineRedirectUrl(authentication);

        // Redirect the user
        response.sendRedirect(redirectUrl);
    }

    private String determineRedirectUrl(Authentication authentication) {
        // Your logic to determine the redirection URL after successful login
        // For example, redirect to the dashboard for authenticated users
        return "/";
    }
}
