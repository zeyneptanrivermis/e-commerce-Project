package com.example.ecommerce_api.services.Order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_api.dto.OrderDTO.RefundResponse;
import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RefundService {
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public RefundService(OrderRepository orderRepo, PaymentRepository paymentRepo) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    /**
     * Gerçek iade işlemini admin onayı üzerine tetikler.
     * @param orderId iade edilecek siparişin ID'si
     * @return Stripe refund cevabı
     */
    @Transactional
    public RefundResponse createRefund(Long orderId) throws StripeException {
        // 1) Siparişi ve ödeme kaydını al
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));
        Payment payment = paymentRepo.findByOrder_OrderId(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Ödeme kaydı bulunamadı: " + orderId));

        // 2) Stripe refund parametrelerini ayarla
        Map<String, Object> params = new HashMap<>();
        String chargeId = payment.getStripeChargeId();
        if (chargeId != null && !chargeId.isBlank()) {
            params.put("charge", chargeId);
        } else {
            String pi = payment.getStripePaymentIntentId();
            if (pi == null || pi.isBlank()) {
                throw new IllegalStateException("Stripe chargeId veya paymentIntentId bulunamadı.");
            }
            params.put("payment_intent", pi);
        }

        // 3) Tam iade için amount parametresi eklemiyoruz
        Refund stripeRefund = Refund.create(params);

        // 4) Veritabanı güncellemesi
        order.setStatus(OrderStatus.REFUNDED);
        orderRepo.save(order);

        payment.setStatus("REFUNDED");
        paymentRepo.save(payment);

        // 5) Cevabı dön
        return new RefundResponse(stripeRefund.getId(), stripeRefund.getStatus());
    }
   /**
     * Admin reddederse iade talebini iptal eder ve statüleri geri alır.
     */
    @Transactional
    public void declineRefund(Long orderId) {
        // 1) Ödeme kaydını al
        Payment payment = paymentRepo.findByOrder_OrderId(orderId)
            .orElseThrow(() -> 
                new EntityNotFoundException("Payment bulunamadı: " + orderId)
            );
        // 2) Payment status'unu önceki duruma çevir
        payment.setStatus("SUCCEEDED");
        paymentRepo.save(payment);

        // 3) Order kaydını al
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> 
                new EntityNotFoundException("Order bulunamadı: " + orderId)
            );
        // 4) Order status'unu önceki aşamaya çevir
        order.setStatus(OrderStatus.ACCEPTED);
        orderRepo.save(order);
    }
}
