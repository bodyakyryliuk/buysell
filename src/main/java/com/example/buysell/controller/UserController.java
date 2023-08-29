package com.example.buysell.controller;

import com.example.buysell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    @PostMapping("/add-money")
    public String addBalance(@RequestParam BigDecimal amount){
        userService.addMoney(amount);
        return "redirect:/";
    }

    @PostMapping("/withdraw-money/{amount}")
    public String withdrawMoney(@PathVariable BigDecimal amount){
        userService.withdrawMoney(amount);
        return "redirect:/";
    }

}
