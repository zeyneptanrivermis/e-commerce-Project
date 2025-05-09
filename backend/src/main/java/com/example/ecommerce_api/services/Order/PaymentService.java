package com.example.ecommerce_api.services.Order;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {
    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amount)                   // kuruş cinsinden
            .setCurrency(currency)
            .addPaymentMethodType("card")
            .build();
        return PaymentIntent.create(params);
    }
}
