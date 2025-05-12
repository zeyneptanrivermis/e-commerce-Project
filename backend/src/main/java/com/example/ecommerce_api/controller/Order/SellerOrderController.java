package com.example.ecommerce_api.controller.Order;

import com.example.ecommerce_api.dto.OrderDTO.OrderDTO;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.UserRepositories.SellerRepository;
import com.example.ecommerce_api.services.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/seller/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SellerRepository sellerRepository;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrdersForSeller(Authentication auth) {
        String sellerEmail = auth.getName();

        Seller seller = sellerRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        List<OrderDTO> orders = orderService.getOrdersBySellerId(seller.getUserId());
        return ResponseEntity.ok(orders);
    }
}
