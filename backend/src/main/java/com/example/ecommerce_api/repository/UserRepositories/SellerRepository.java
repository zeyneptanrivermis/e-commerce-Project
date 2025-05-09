package com.example.ecommerce_api.repository.UserRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ecommerce_api.entity.UserEntity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByEmail(String email);

    boolean existsByEmail(String email);
}
