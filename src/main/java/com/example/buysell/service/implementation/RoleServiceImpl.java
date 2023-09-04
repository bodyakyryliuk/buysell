package com.example.buysell.service.implementation;

import com.example.buysell.model.Role;
import com.example.buysell.model.UserRole;
import com.example.buysell.repository.RoleRepository;
import com.example.buysell.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByUserRole(UserRole userRole) {
//        return roleRepository.findFirstByUserRole(userRole);
        return null;
    }
}
