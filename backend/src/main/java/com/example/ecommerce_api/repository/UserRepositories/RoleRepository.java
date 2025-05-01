package com.example.ecommerce_api.repository.UserRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.UserEntity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // ROLE_ADMIN, ROLE_CUSTOMER ROLE_SELLER
}
