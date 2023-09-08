package com.example.buysell.service.admin;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import com.example.buysell.model.UserRole;
import com.example.buysell.repository.UserRepository;
import com.example.buysell.service.role.RoleService;
import com.example.buysell.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserServiceImpl userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    @Override
    public List<User> getAllUsers() {
        Role roleUser = roleService.findByUserRole(UserRole.ROLE_USER);
        return userService.getAllUsersByRole(roleUser);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    public User save(User user){
        return userRepository.save(user);
    }

}
