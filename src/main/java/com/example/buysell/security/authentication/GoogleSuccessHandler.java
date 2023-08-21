package com.example.buysell.security.authentication;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("In onAuthenticationSuccess");

        if (authentication.getPrincipal() instanceof DefaultOAuth2User){
            log.info("in authentication.getCredentials() instanceof DefaultOAuth2User");
            OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;

            // Get the user attributes from the authentication token
            Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("USER"));

            DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(authorities, attributes , "email");


            if(userRepository.findByEmailAndAuthMethod(defaultOAuth2User.getAttribute("email"),
                       "google") == null) {

                save(defaultOAuth2User);
            }

            OAuth2AuthenticationToken updatedAuthentication = new OAuth2AuthenticationToken(
                    defaultOAuth2User,
                    authorities,
                    authenticationToken.getAuthorizedClientRegistrationId()
            );
            updatedAuthentication.setDetails(authenticationToken.getDetails());

            // Set the updated authentication in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

            log.info("Sending redirect");
            response.sendRedirect("/");
        }else
            response.sendRedirect("/login");
    }

    public void save(OAuth2User oAuth2User) {
        User user = new User();

        // assign user details to the user object
        user.setFullName(oAuth2User.getAttribute("name"));
        user.setLocale(oAuth2User.getAttribute("locale"));
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setActive(true);
        user.setProfilePicture(oAuth2User.getAttribute("picture"));
        user.setAuthIdentifier(oAuth2User.getAttribute("sub"));
        user.setAuthMethod("google");
        // give user default role of "employee"
        user.setRoles(List.of(new Role(user.getEmail(), "USER")));

        // save user in the database
        userRepository.save(user);
    }

}
