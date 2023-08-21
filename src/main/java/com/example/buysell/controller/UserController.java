package com.example.buysell.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/roles")
    public String getRoles(Authentication authentication) {
        StringBuilder stringBuilder = new StringBuilder();

        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                stringBuilder.append(authority.getAuthority()).append(" ");
            }
        }

        return stringBuilder.toString();
    }

}
