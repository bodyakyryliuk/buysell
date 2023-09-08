package com.example.buysell.controller;

import com.example.buysell.service.user.UserService;
import com.example.buysell.service.user.UserServiceImpl;
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
        return userService.getRolesString(authentication);
    }

    @GetMapping("/add-money")
    public String showAddFundsPage(){
        return "add-funds-page";
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
