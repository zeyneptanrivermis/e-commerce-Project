package com.example.ecommerce_api.controller.Order;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.entity.OrderEntity.Shipping;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;
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

    private UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // 🔵 Müşterinin kendi sepetinden sipariş oluşturur
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody Order incomingOrder, Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        
        // Müşteriyi veritabanından al
        Customer customer = (Customer) userRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    
        // Siparişe müşteri ata
        incomingOrder.setCustomer(customer);
    
        Order savedOrder = orderService.createOrderWithItems(incomingOrder);
        return ResponseEntity.ok(savedOrder);
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
        String email = principal.getName();
        return userRepository                                // ← static çağrı yerine örnek üzerinden
            .findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"))
            .getUserId();
    }
}
