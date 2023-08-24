package com.example.buysell.security.authentication;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.model.UserRole;
import com.example.buysell.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GoogleSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;

    public GoogleSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;

            OAuth2User oauth2User = authenticationToken.getPrincipal();

            String email = oauth2User.getAttribute("email");

            // Check if the user exists in the database
            User user = userRepository.findByEmailAndAuthMethod(email, "google");
            if (user == null) {
                // If the user doesn't exist, create and save the user
                user = saveUserFromOAuth2User(oauth2User);
            }

            OAuth2AuthenticationToken updatedAuthentication = new OAuth2AuthenticationToken(
                    oauth2User,
                    user.getRoles(),
                    authenticationToken.getAuthorizedClientRegistrationId()
                    );
            updatedAuthentication.setDetails(authenticationToken.getDetails());

            // Set the updated authentication in the SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);


            // Redirect to the desired URL after successful authentication
            response.sendRedirect("/");
        } else {
            response.sendRedirect("/login");
        }
    }

    private User saveUserFromOAuth2User(OAuth2User oauth2User) {
        User user = new User();

        // Assign user details from the OAuth2 user attributes
        user.setFullName(oauth2User.getAttribute("name"));
        user.setLocale(oauth2User.getAttribute("locale"));
        user.setEmail(oauth2User.getAttribute("email"));
        user.setActive(true);
        user.setProfilePicture(oauth2User.getAttribute("picture"));
        user.setAuthIdentifier(oauth2User.getAttribute("sub"));
        user.setAuthMethod("google");
        user.setRoles(List.of(new Role(UserRole.ROLE_USER)));

        // Save user in the database
        return userRepository.save(user);

    }
}
