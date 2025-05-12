package com.example.ecommerce_api.controller.Order;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.dto.OrderDTO.PaymentCompleteRequest;
import com.example.ecommerce_api.services.Order.OrderService;
import com.stripe.exception.StripeException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private OrderService orderService;

    /**
     * Ön koşul: orderService.createStripePayment ile daha önce bir Payment kaydı
     * ve PaymentIntentId yaratılmış olmalı.
     */
    @PostMapping("/complete/{orderId}")
    public ResponseEntity<Void> completePayment(
            @PathVariable Long orderId,
            @RequestBody PaymentCompleteRequest req
    ) {
        orderService.finalizePayment(orderId, req); // 🔄 DTO tüm verileri taşıyor
        return ResponseEntity.ok().build();
    }


        @PostMapping("/create-intent/{orderId}")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> body) {
        String currency = body.get("currency");
        try {
            String clientSecret = orderService.createStripePayment(orderId, currency);
            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", clientSecret);
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            e.printStackTrace();
            // Hata durumunda 500 dön
            return ResponseEntity.status(500).body(Map.of("error", "Stripe payment intent oluşturulamadı: " + e.getMessage()));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
}
