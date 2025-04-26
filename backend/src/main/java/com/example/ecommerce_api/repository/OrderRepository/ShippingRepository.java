package com.example.ecommerce_api.repository.OrderRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.Shipping;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
    // Sipariş bazlı kargo bilgisini getir
    Optional<Shipping> findByOrder(Order order);
}