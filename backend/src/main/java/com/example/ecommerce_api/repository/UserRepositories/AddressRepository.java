package com.example.ecommerce_api.repository.UserRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.UserEntity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser_UserId(Long userId); 
    // Bir kullanıcının tüm adreslerini listelemek için
}
