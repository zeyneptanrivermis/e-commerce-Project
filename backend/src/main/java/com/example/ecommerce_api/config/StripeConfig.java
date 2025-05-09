package com.example.ecommerce_api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import com.stripe.Stripe;                 // Stripe sınıfı buradan geliyor

@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        // Aşağıdaki satır mutlaka bu şekilde, "Stripe" değil "Stripe.apiKey" olmalı:
        Stripe.apiKey = secretKey;
    }
}