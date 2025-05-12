package com.example.ecommerce_api.dto.OrderDTO;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompleteRequest {
    private String paymentIntentId;
    private Long amount;
    private String chargeId; // Optional field

    public PaymentCompleteRequest(String id) {
    }
    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getChargeId() {
        return chargeId;
    }
    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }
}