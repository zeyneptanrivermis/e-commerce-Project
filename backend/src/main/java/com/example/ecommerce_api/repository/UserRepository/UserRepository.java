package com.example.ecommerce_api.repository.UserRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.UserEntity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email); // Login için şart
}
