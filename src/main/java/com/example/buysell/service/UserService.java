package com.example.buysell.service;

import com.example.buysell.model.*;
import com.example.buysell.repository.RoleRepository;
import com.example.buysell.repository.UserRepository;
import com.example.buysell.repository.VerificationTokenRepository;
import com.example.buysell.user.WebUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;

    public User save(WebUser webUser) {
        User user = new User();

        // assign user details to the user object
        user.setFullName(webUser.getFullName());
        user.setPassword(passwordEncoder.encode(webUser.getPassword()));
        user.setEmail(webUser.getEmail());
        user.setActive(false);
        user.setAuthMethod("email");
        user.setBalance(BigDecimal.ZERO);
        user.setShoppingCart(new ShoppingCart(user));

        Role roleUser = roleRepository.findByUserRole(UserRole.ROLE_USER);
        if(roleUser == null){
            roleUser = new Role(UserRole.ROLE_USER);
            roleRepository.save(roleUser);
        }
        user.setRoles(List.of(roleUser));

        // save user in the database
        return userRepository.save(user);
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return getUserByEmail(userDetails.getUsername());
        }else if (authentication.getPrincipal() instanceof OAuth2User oAuth2User){
            return getUserByEmail(oAuth2User.getAttribute("email"));
        }
        return null;
    }
    public void removeUser(Long id){
        userRepository.deleteById(id);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByToken(String token){
        return verificationTokenRepository.findByToken(token).getUser();
    }

    public void createVerificationToken(User user, String token){
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }

    public VerificationToken getVerificationToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    public void saveRegisteredUser(User user){
        userRepository.save(user);
    }

    public void addMoney(BigDecimal amount){
        User user = getLoggedInUser();
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
    public void withdrawMoney(BigDecimal amount){
        User user = getLoggedInUser();
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }

    public List<User> getAllUsersByRole(Role role){
        return userRepository.findAllByRole(role);
    }
}




