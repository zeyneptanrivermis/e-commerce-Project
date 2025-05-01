package com.example.ecommerce_api.repository.UserRepositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.UserEntity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{

}
