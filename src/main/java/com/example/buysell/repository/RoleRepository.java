package com.example.buysell.repository;

import com.example.buysell.model.Role;
import com.example.buysell.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.role = :userRole")
    Role findByUserRole(@Param("userRole") UserRole userRole);
}
