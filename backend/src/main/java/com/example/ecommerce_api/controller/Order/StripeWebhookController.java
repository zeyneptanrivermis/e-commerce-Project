package com.example.ecommerce_api.controller.Order;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/api/stripe/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject()
                .orElse(null);

            if (intent != null && intent.getLatestCharge() != null) {
                String chargeId = intent.getLatestCharge(); // ✅ Gerçek ch_... id
                String paymentIntentId = intent.getId();

                Payment payment = paymentRepository.findByStripePaymentIntentId(paymentIntentId)
                    .orElse(null);

                if (payment != null) {
                    payment.setStripeChargeId(chargeId);
                    payment.setStatus("succeeded");
                    payment.setPaymentDate(LocalDate.now());
                    paymentRepository.save(payment);
                    System.out.println("✅ Charge ID kaydedildi: " + chargeId);
                } else {
                    System.err.println("❌ Payment bulunamadı, PI ID: " + paymentIntentId);
                }
            }
        }

        return ResponseEntity.ok("Webhook işleme alındı");
    }
}
