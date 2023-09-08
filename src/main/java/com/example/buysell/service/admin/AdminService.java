package com.example.buysell.service.admin;

import com.example.buysell.model.Role;
import com.example.buysell.model.User;
import java.util.List;

public interface AdminService {
    List<User> getAllUsers();

    List<Role> getAllRoles();

    User save(User user);
}
