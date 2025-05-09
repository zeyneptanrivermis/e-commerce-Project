package com.example.ecommerce_api.controller.Order;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.dto.OrderDTO.PaymentCompleteRequest;
import com.example.ecommerce_api.services.Order.OrderService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {


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
        orderService.finalizePayment(orderId, req.getPaymentIntentId());
        return ResponseEntity.ok().build();
    }
}
