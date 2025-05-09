package com.example.ecommerce_api.controller.Order;

import com.example.ecommerce_api.dto.OrderDTO.OrderDTO;
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

    // 🔵 Frontend artık boş body veya hiç body göndermeden bu endpoint'i çağıracak
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        // Sepetteki ürünleri alıp yeni Order ve OrderItem'ları oluşturan metod
        Order savedOrder = orderService.createOrder(customerId);
        return ResponseEntity.ok(savedOrder);
    }


    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getUserOrders(Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"))
                .getUserId();

        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
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
