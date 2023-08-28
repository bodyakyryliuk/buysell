package com.example.buysell.repository;

import com.example.buysell.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndAuthMethod(String email, String authMethod);
}
