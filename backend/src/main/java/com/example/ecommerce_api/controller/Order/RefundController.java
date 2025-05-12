package com.example.ecommerce_api.controller.Order;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.dto.OrderDTO.RefundRequest;
import com.example.ecommerce_api.dto.OrderDTO.RefundResponse;
import com.example.ecommerce_api.services.Order.RefundService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api/orders")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping("/{orderId}/refund")
    @PreAuthorize("@authService.canAccessOrder(principal, #orderId)")
    public ResponseEntity<RefundResponse> refundOrder(
            @PathVariable Long orderId,
             @RequestBody(required = false) RefundRequest request
    ) throws StripeException {
        BigDecimal amount = (request != null) ? request.getAmount() : null;
        RefundResponse response = refundService.createRefund(orderId);
        return ResponseEntity.ok(response);
    }
}