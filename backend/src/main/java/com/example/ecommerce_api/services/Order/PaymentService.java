package com.example.ecommerce_api.services.Order;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.dto.OrderDTO.PaymentIntentDto;
import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;

import jakarta.transaction.Transactional;


@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;

    public PaymentService(PaymentRepository paymentRepo,
                          OrderRepository orderRepo) {
        this.paymentRepo = paymentRepo;
        this.orderRepo   = orderRepo;
    }    
    
    @Transactional
    public PaymentIntentDto createPaymentIntent(Long orderId) throws StripeException {
        // 1) Order’ı çek
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order bulunamadı: " + orderId));

        // 2) Payment kaydını getir ya da yenisini oluştur
        Payment payment = paymentRepo.findByOrder_OrderId(orderId)
            .orElseGet(() -> {
                Payment p = new Payment();
                p.setOrder(order);
                p.setCustomer(order.getCustomer());
                return p;
            });

        // 3) Stripe ile Intent oluştur
        BigDecimal amount = BigDecimal.valueOf(order.getOrderTotalWithDiscount());
        PaymentIntent intent = PaymentIntent.create(
            PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                .setCurrency("try")
                .addPaymentMethodType("card")
                .build()
        );

        // 4) Payment entity’sini güncelle ve kaydet
        payment.setAmount(order.getOrderTotalWithDiscount());
        payment.setStripePaymentIntentId(intent.getId());
        payment.setStatus(intent.getStatus());
        paymentRepo.save(payment);

        // 5) Front-end’e clientSecret dön
        return new PaymentIntentDto(intent.getClientSecret());
    }
}
