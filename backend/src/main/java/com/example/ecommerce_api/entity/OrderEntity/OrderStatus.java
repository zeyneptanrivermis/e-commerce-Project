package com.example.ecommerce_api.entity.OrderEntity;

public enum OrderStatus {
    PENDING,
    ACCEPTED,
    COMPLETED,
    CANCELLED, 
    SHIPPED,

    // —— İADE SÜRECİ —— 
    REFUND_REQUESTED,     // Müşteri iade talebi açtı
    REFUND_APPROVED,      // Admin iadesini onayladı
    REFUND_DECLINED,      // Admin iadesini reddetti
    REFUNDED              // Stripe üzerinden iade gerçekleşti
}