package com.example.buysell.service.user;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.model.VerificationToken;
import com.example.buysell.user.WebUser;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User save(WebUser webUser);

    User getLoggedInUser();

    User getUserByEmail(String email);

    User getUserByToken(String token);

    User getUserById(Long id);

    void removeUser(Long id);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String token);

    User saveRegisteredUser(User user);

    void addMoney(BigDecimal amount);

    void withdrawMoney(BigDecimal amount);

    List<User> getAllUsersByRole(Role role);

    String getRolesString(Authentication authentication);

    User findByEmailAndAuthMethod(String email,String authMethod);
}
