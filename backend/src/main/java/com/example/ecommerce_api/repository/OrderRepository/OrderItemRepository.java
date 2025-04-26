package com.example.ecommerce_api.repository.OrderRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Bir siparişe ait tüm ürünleri getir
    List<OrderItem> findByOrder(Order order);
}
