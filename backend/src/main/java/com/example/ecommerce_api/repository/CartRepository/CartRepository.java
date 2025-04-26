package com.example.ecommerce_api.repository.CartRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.UserEntity.Customer;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer(Customer customer); // belirli müşterinin sepeti
}
