package com.example.ecommerce_api.dto.OrderDTO;

public class PaymentIntentDto {
    private String clientSecret;

    public PaymentIntentDto() {}

    public PaymentIntentDto(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}