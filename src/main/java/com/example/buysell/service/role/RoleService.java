package com.example.buysell.service.role;

import com.example.buysell.model.Role;
import com.example.buysell.model.UserRole;

import java.util.List;

public interface RoleService {
    Role findByUserRole(UserRole userRole);

    List<Role> findAll();

}
