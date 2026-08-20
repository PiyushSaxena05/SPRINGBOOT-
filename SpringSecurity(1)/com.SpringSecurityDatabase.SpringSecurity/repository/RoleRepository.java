package com.SpringSecurityDatabase.SpringSecurity.repository;

import com.SpringSecurityDatabase.SpringSecurity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
   
}
