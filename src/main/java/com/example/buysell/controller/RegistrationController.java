package com.example.buysell.controller;

import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.service.registration.RegistrationService;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {
    private final EmailAuthenticationProvider emailAuthenticationProvider;
    private final ApplicationEventPublisher eventPublisher;
    private final RegistrationService registrationService;


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("webUser", new WebUser());
        return "signup";
    }

    @PostMapping("/processRegistration")
    public String processRegistration(
            @Valid @ModelAttribute("webUser") WebUser webUser,
            BindingResult bindingResult,
            HttpServletRequest request,
            Model model
    ){
        if(bindingResult.hasErrors()){
            return "signup";
        }

        // check database if user exists -> true if user exists

        if(registrationService.checkUserExists(webUser.getEmail())){
            model.addAttribute("webUser", new WebUser());
            model.addAttribute("registrationError", "User with given email already exists");
            log.warn("User with given email already exists: " + webUser.getEmail());

            return "signup";
        }

        registrationService.processRegistration(webUser, request, eventPublisher);
        return "registration-confirm";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, WebRequest request, Model model){
        Locale locale = request.getLocale();

        // true if token == null
        if (registrationService.checkVerificationToken(token)) {
            String message = "Invalid token";
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        // true if time expired
        if (registrationService.checkTokenExpiryDate(token)) {
            String messageValue = "Authorization time expired!";
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        registrationService.confirmRegistration(token, emailAuthenticationProvider);
        model.addAttribute("activated", true);

        return "registration-confirm";
    }



}
