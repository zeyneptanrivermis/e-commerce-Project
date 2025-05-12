package com.example.ecommerce_api.services.User;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.dto.OrderDTO.RefundResponse;
import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AdminRefundService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public AdminRefundService(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            @Value("${stripe.api.key}") String stripeSecretKey) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Admin onayıyla iade talebini Stripe'da işleme alıp durumu günceller.
     */
    @Transactional
    public RefundResponse approveRefund(Long orderId) {
        // 1) Siparişi yükle ve durum kontrolü
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (order.getStatus() != OrderStatus.REFUND_REQUESTED) {
            throw new IllegalStateException("Order is not in REFUND_REQUESTED status.");
        }

        // 2) Ödeme kaydını yükle
        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
            .orElseThrow(() -> new RuntimeException("Payment record not found for order id: " + orderId));

        // 3) Stripe refund isteği
        Map<String, Object> params = new HashMap<>();
        params.put("charge", payment.getStripeChargeId());
        BigDecimal amount = BigDecimal.valueOf(payment.getAmount());
        params.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());

        Refund stripeRefund;
        try {
            stripeRefund = Refund.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe refund failed: " + e.getMessage(), e);
        }

        // 4) Durum güncellemeleri
        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);

        payment.setStatus("REFUNDED");
        paymentRepository.save(payment);

        // 5) Yanıt
        return new RefundResponse(stripeRefund.getId(), stripeRefund.getStatus());
    }

    /**
     * Admin iade talebini reddeder, durumu günceller.
     */
    @Transactional
    public void declineRefund(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        if (order.getStatus() != OrderStatus.REFUND_REQUESTED) {
            throw new IllegalStateException("Order is not in REFUND_REQUESTED status.");
        }

        order.setStatus(OrderStatus.REFUND_DECLINED);
        orderRepository.save(order);
        // İsteğe bağlı: reason bilgisini loglayabilir veya kullanıcıya bildirim atabilirsiniz
    }
}
