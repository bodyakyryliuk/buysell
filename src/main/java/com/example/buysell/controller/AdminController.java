package com.example.buysell.controller;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.model.UserRole;
import com.example.buysell.repository.RoleRepository;
import com.example.buysell.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping
    public String getAdminPage(Model model){
        Role roleUser = roleRepository.findByUserRole(UserRole.ROLE_USER);
        List<User> usersUser = userService.getAllUsersByRole(roleUser);

        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("users", usersUser);
        model.addAttribute("allRoles", allRoles);
        return "administrator-page";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("user") User user){
        userService.save(user);
        return "redirect:/admin";
    }
}
