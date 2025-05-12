package com.example.ecommerce_api.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.entity.UserEntity.Customer;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Belirli bir siparişin ödeme kaydını getir
    Optional<Payment> findByOrder(Order order);

    // Müşteri bazlı ödemeler
    List<Payment> findByCustomer(Customer customer);

    Optional<Payment> findByStripePaymentIntentId(String intentId);

    Optional<Payment> findByOrder_OrderId(Long orderId);
    
}
