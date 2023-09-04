package com.example.buysell.controller;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.model.UserRole;
import com.example.buysell.repository.RoleRepository;
import com.example.buysell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping("/admin")
    public String getAdminPage(Model model){
        Role role = roleRepository.findByUserRole(UserRole.ROLE_USER);
        List<User> users = userService.getAllUsersByRole(role);
        model.addAttribute("users", users);
        return "administrator-page";
    }
}
