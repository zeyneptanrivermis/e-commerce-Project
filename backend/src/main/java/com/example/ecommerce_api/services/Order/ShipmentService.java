package com.example.ecommerce_api.services.Order;

import java.nio.file.AccessDeniedException;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;

@Service
public class ShipmentService {
    private final OrderRepository orderRepo;

    public ShipmentService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    /**
     * Kullanıcıya ait ve var olan bir siparişin durumunu döner
     * @throws AccessDeniedException 
     */
    public ShipmentStatusDTO getStatus(Long customerId, Long orderId) throws AccessDeniedException {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Sadece sahibi görebilsin
        if (!order.getCustomer().getUserId().equals(customerId)) {
            throw new AccessDeniedException("You are not allowed to view this order’s shipment status.");
        }

        return new ShipmentStatusDTO(orderId, order.getStatus().name());
    }
}