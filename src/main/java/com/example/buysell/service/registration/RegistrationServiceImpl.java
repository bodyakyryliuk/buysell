package com.example.buysell.service.registration;

import com.example.buysell.model.*;
import com.example.buysell.security.authentication.EmailAuthenticationProvider;
import com.example.buysell.security.authentication.OnRegistrationCompleteEvent;
import com.example.buysell.service.role.RoleService;
import com.example.buysell.service.user.UserService;
import com.example.buysell.user.WebUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserService userService;
    private final RoleService roleService;

    // return true if user exists
    @Override
    public boolean checkUserExists(String email){
        return userService.getUserByEmail(email) != null;
    }

    @Override
    public boolean checkVerificationToken(String token) {
        return userService.getVerificationToken(token) == null;
    }

    @Override
    public boolean checkTokenExpiryDate(String token) {
        Calendar calendar = Calendar.getInstance();
        // return true if token time expired
        return userService.getVerificationToken(token).getExpiryDate().getTime() - calendar.getTime().getTime() <= 0;
    }

    @Override
    public void processRegistration(WebUser webUser, HttpServletRequest request, ApplicationEventPublisher eventPublisher){
        User user = userService.save(webUser);
        Locale locale = request.getLocale();
        String appUrl = request.getContextPath();

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, locale, appUrl));
    }

    @Override
    public void confirmRegistration(String token, EmailAuthenticationProvider authenticationProvider) {
        User user = userService.getUserByToken(token);
        user.setActive(true);
        authenticationProvider.autoLogin(user.getEmail(), user.getPassword());
    }

    @Override
    @Transactional
    public User saveUserFromOAuth2User(OAuth2User oauth2User) {
        User user = new User();

        // Assign user details from the OAuth2 user attributes
        user.setFullName(oauth2User.getAttribute("name"));
        user.setLocale(oauth2User.getAttribute("locale"));
        user.setEmail(oauth2User.getAttribute("email"));
        user.setActive(true);
        user.setProfilePicture(oauth2User.getAttribute("picture"));
        user.setAuthIdentifier(oauth2User.getAttribute("sub"));
        user.setAuthMethod("github");
        user.setBalance(BigDecimal.ZERO);
        user.setShoppingCart(new ShoppingCart(user));

        Role roleUser = roleService.findByUserRole(UserRole.ROLE_USER);
        if(roleUser == null){
            roleUser = new Role(UserRole.ROLE_USER);
            roleService.save(roleUser);
        }
        user.setRoles(List.of(roleUser));


        // Save user in the database
        return userService.saveRegisteredUser(user);
    }

    @Override
    public boolean createOAuth2Token(Authentication authentication, HttpServletResponse response) {
        if(authentication instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oAuth2User = token.getPrincipal();

            User user = userService.findByEmailAndAuthMethod(oAuth2User.getAttribute("email"), "github");
            if (user == null)
                user = saveUserFromOAuth2User(oAuth2User);

            OAuth2AuthenticationToken updatedAuthentication = new OAuth2AuthenticationToken(
                    oAuth2User,
                    user.getRoles(),
                    token.getAuthorizedClientRegistrationId()
            );
            updatedAuthentication.setDetails(token.getDetails());

            // Set the updated authentication in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

            return true;
        }
        return false;
    }

}












