package com.example.ecommerce_api.dto.OrderDTO;

import java.math.BigDecimal;

public class RefundRequest {

    private BigDecimal amount;

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}