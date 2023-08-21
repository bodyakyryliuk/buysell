package com.example.buysell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "login-page";
    }

    @GetMapping("/login/email")
    public String emailLogin(){
        return "email-login-page";
    }

    @GetMapping("/login/google")
    public String googleLogin(){
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/login/facebook")
    public String facebookLogin(){
        return "email-login-page";
    }


}









