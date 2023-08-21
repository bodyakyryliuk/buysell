package com.example.buysell.controller;

import com.example.buysell.model.User;
import com.example.buysell.repository.UserRepository;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.service.UserService;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {
    @Autowired
    private final UserService userService;
    private final EmailAuthenticationProvider emailAuthenticationProvider;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("webUser", new WebUser());
        log.info("in signup");
        return "signup";
    }

    @PostMapping("/processRegistration")
    public String processRegistration(
            @Valid @ModelAttribute("webUser") WebUser webUser,
            BindingResult bindingResult,
            HttpSession httpSession,
            Model model
    ){
        log.info("Processing registration");
        String email = webUser.getEmail();

        if(bindingResult.hasErrors()){
            return "signup";
        }

        // check database if user exists

        User existing = userService.getUserByEmail(email);
        if(existing != null){
            model.addAttribute("webUser", new WebUser());
            model.addAttribute("registrationError", "User with given email already exists");
            log.warn("User with given email already exists: " + existing.getEmail());

            return "signup";
        }

        userService.save(webUser);
        log.info("Successfully created user: " + email);


        return "redirect:/";
    }


}
