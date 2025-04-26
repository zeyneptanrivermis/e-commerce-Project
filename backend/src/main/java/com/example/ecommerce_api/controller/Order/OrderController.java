package com.example.ecommerce_api.controller.Order;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.entity.OrderEntity.Shipping;
import com.example.ecommerce_api.services.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 🔵 Müşterinin kendi sepetinden sipariş oluşturur
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        Order order = orderService.createOrder(customerId);
        return ResponseEntity.ok(order);
    }

    // 🔵 Müşterinin tüm siparişlerini getir
    @GetMapping
    public ResponseEntity<List<Order>> getCustomerOrders(Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        List<Order> orders = orderService.getOrdersByCustomer(customerId);
        return ResponseEntity.ok(orders);
    }

    // 🔵 Bir siparişe ödeme ekle
    @PostMapping("/{orderId}/payment")
    public ResponseEntity<Payment> addPayment(@PathVariable Long orderId, @RequestParam double amount) {
        Payment payment = orderService.addPaymentToOrder(orderId, amount);
        return ResponseEntity.ok(payment);
    }

    // 🔵 Bir siparişe shipping bilgisi ekle
    @PostMapping("/{orderId}/shipping")
    public ResponseEntity<Shipping> addShipping(@PathVariable Long orderId, @RequestBody Shipping shippingInfo) {
        Shipping shipping = orderService.addShippingToOrder(orderId, shippingInfo);
        return ResponseEntity.ok(shipping);
    }

    // Yardımcı method: Principal'dan customerId çıkar
    private Long getCustomerIdFromPrincipal(Principal principal) {
        // Burada principal.getName() ile email geliyor, ama 
        // istersen UserService ile email -> id çözümünü dışarı alabiliriz.
        throw new UnsupportedOperationException("Principal çözümleme henüz tamamlanmadı.");
    }
}
