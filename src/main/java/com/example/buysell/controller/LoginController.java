package com.example.buysell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login(){
        return "login-page";
    }

    @GetMapping("/email")
    public String emailLogin(){
        return "email-login-page";
    }

    @GetMapping("/google")
    public String googleLogin(){
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/facebook")
    public String facebookLogin(){
        return "email-login-page";
    }

}









