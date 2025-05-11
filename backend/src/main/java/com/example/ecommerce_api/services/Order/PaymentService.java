package com.example.ecommerce_api.services.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import com.stripe.model.Charge;
import com.stripe.Stripe;
import com.stripe.model.ChargeCollection;

@Service
public class PaymentService {

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amount)
            .setCurrency(currency)
            .addPaymentMethodType("card")
            .build();

        return PaymentIntent.create(params);
    }

    public void refundCharge(String chargeId) throws StripeException {
        RefundCreateParams params = RefundCreateParams.builder()
            .setCharge(chargeId)
            .build();
        Refund.create(params);
    }

}
