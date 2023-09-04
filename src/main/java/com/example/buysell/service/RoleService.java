package com.example.buysell.service;

import com.example.buysell.model.Role;
import com.example.buysell.model.UserRole;

public interface RoleService {
    Role findByUserRole(UserRole userRole);
}
