package com.example.ecommerce_api.controller.Order;

import com.example.ecommerce_api.services.Order.OrderService;
import com.example.ecommerce_api.dto.OrderDTO.PaymentCompleteRequest;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook/stripe")
public class PaymentWebhookController {

    @Autowired
    private OrderService orderService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody String payload) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        switch (event.getType()) {
            case "payment_intent.succeeded": {
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElseThrow();
                Long orderId = Long.valueOf(intent.getMetadata().get("orderId"));
                String chargeId = intent.getLatestCharge();
                Long amount = intent.getAmount();

                PaymentCompleteRequest paymentRequest = new PaymentCompleteRequest(
                    intent.getId(),  // paymentIntentId
                    amount,          // amount
                    chargeId         // chargeId
                );

                orderService.finalizePayment(orderId, paymentRequest);
                break;
            }
            case "payment_intent.payment_failed": {
                PaymentIntent failedIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElseThrow();
                Long failedOrderId = Long.valueOf(failedIntent.getMetadata().get("orderId"));
                orderService.updateStatus(failedOrderId, OrderStatus.CANCELLED);
                break;
            }
            default:
                // ignore other events
                break;
        }

        return ResponseEntity.ok("Received");
    }
}