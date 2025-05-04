package com.example.ecommerce_api.services.Chat;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;

@Service
public class ChatService {
    private final OrderRepository orderRepository;

    public ChatService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String generateReply(String message, Customer customer) {
        message = message.toLowerCase();

        if (message.contains("hello") || message.contains("hi")) {
            return "Hello! How can I assist you today?";
        }

        if (message.contains("tracking") || message.contains("kargo")) {
            Optional<Order> latestOrder = orderRepository.findTopByCustomerOrderByOrderIdDesc(customer);
            if (latestOrder.isPresent()) {
                Long orderId = latestOrder.get().getOrderId();
                return "Your latest order (Order ID: #" + orderId + ") is currently being prepared for shipment.";
            } else {
                return "You don’t have any orders yet.";
            }
        }

        if (message.contains("order") || message.contains("sipariş")) {
            return "You can view your order history under the 'My Orders' section.";
        }

        if (message.contains("refund") || message.contains("iade")) {
            return "To request a refund, please visit the 'Returns' section. Refunds are processed within 5 business days.";
        }

        if (message.contains("cancel") || message.contains("iptal")) {
            return "If your order has not shipped yet, you can cancel it from the 'My Orders' page.";
        }

        return "Sorry, I didn’t understand that. Could you please rephrase your question?";
    }
    
}
