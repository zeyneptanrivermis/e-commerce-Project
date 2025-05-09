package com.example.ecommerce_api.dto.OrderDTO;

public class PaymentCompleteRequest {
    private String paymentIntentId;
    private Long amount;      

    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }
}