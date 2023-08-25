package com.example.buysell.controller;

import com.example.buysell.model.User;
import com.example.buysell.model.VerificationToken;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.security.authentication.OnRegistrationCompleteEvent;
import com.example.buysell.service.UserService;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.Locale;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final EmailAuthenticationProvider emailAuthenticationProvider;
    private final ApplicationEventPublisher eventPublisher;


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
            HttpServletRequest request,
            Model model
    ){
        long time = System.currentTimeMillis();
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

        User user = userService.save(webUser);
        Locale locale = request.getLocale();
        String appUrl = request.getContextPath();

        long secTime = System.currentTimeMillis() - time;
        log.info("Successfully created user: " + email + " with time " + secTime);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, locale, appUrl));

        log.info("After event publisher with time " + (System.currentTimeMillis() - secTime));

        return "registration-confirm";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, WebRequest request, Model model){
        log.info("At the beginning of ConfirmRegistration method");

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = "Invalid token";
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }


        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = "Authorization time expired!";
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }


        user.setActive(true);
        userService.saveRegisteredUser(user);
        model.addAttribute("activated", true);
        emailAuthenticationProvider.autoLogin(user.getEmail(), user.getPassword());
        log.info("In ConfirmRegistration method: after auto login");

        SecurityContext sc = SecurityContextHolder.getContext();
        log.info("After autologin method (directly before redirecting): " + String.valueOf(sc.getAuthentication()));


        return "registration-confirm";
    }



}
