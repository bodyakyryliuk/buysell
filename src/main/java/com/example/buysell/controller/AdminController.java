package com.example.buysell.controller;

import com.example.buysell.model.User;
import com.example.buysell.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    @GetMapping
    public String getAdminPage(Model model){
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("allRoles", adminService.getAllRoles());
        return "administrator-page";
    }

    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("user") User user){
        adminService.save(user);
        return "redirect:/admin";
    }
}
