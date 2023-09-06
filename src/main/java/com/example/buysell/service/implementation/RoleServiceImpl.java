package com.example.buysell.service.implementation;

import com.example.buysell.model.Role;
import com.example.buysell.model.UserRole;
import com.example.buysell.repository.RoleRepository;
import com.example.buysell.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @PostConstruct
    public void init(){
        Role roleUser = findByUserRole(UserRole.ROLE_USER);
        if (roleUser == null)
            roleRepository.save(new Role(UserRole.ROLE_USER));

        Role roleSeller = findByUserRole(UserRole.ROLE_SELLER);
        if (roleSeller == null)
            roleRepository.save(new Role(UserRole.ROLE_SELLER));

        Role roleManager = findByUserRole(UserRole.ROLE_MANAGER);
        if (roleManager == null)
            roleRepository.save(new Role(UserRole.ROLE_MANAGER));

        Role roleAdmin = findByUserRole(UserRole.ROLE_ADMIN);
        if (roleAdmin == null)
            roleRepository.save(new Role(UserRole.ROLE_ADMIN));
    }

    @Override
    public Role findByUserRole(UserRole userRole) {
        return roleRepository.findByUserRole(userRole);
    }
}
