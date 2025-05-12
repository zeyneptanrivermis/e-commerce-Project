package com.example.ecommerce_api.controller.User;

import com.example.ecommerce_api.dto.OrderDTO.OrderDTO;
import com.example.ecommerce_api.dto.OrderDTO.RefundResponse;
import com.example.ecommerce_api.dto.ProductDTO.CategoryProductCountDTO;
import com.example.ecommerce_api.dto.ProductDTO.ProductDTO;
import com.example.ecommerce_api.dto.UserDTO.AdminUserDTO;
import com.example.ecommerce_api.dto.UserDTO.StatsDTO;
import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.example.ecommerce_api.repository.ProductRepository.CategoryCountProjection;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.services.Product.ProductService;
import com.example.ecommerce_api.services.User.AdminRefundService;
import com.example.ecommerce_api.services.User.AdminService;
import com.example.ecommerce_api.services.User.CustomerService;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.example.ecommerce_api.services.Order.OrderService;
import com.example.ecommerce_api.services.Order.RefundService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;
    private final AdminRefundService adminRefundService;
    private final RefundService refundService;
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired
    private OrderRepository orderRepo;


    public AdminController(AdminService adminService,
                           OrderService orderService,
                           CustomerService customerService,
                           ProductService productService,
                           AdminRefundService adminRefundService,
                           RefundService refundService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.adminRefundService = adminRefundService;
        this.refundService = refundService;
    }

    // --- Admin Users ---
    @GetMapping("/all-admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // --- Statistics ---
    @GetMapping("/stats")
    public ResponseEntity<StatsDTO> getStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    @GetMapping("/customers")
    public ResponseEntity<List<AdminUserDTO>> getAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomersDto());
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<AdminUserDTO>> getAllSellers() {
        return ResponseEntity.ok(adminService.getAllSellersDto());
    }

    // ────────────────────────────────────────────────
    //  Customer Ban/Unban
    // ────────────────────────────────────────────────
    @PutMapping("/customers/{id}/ban")
    public ResponseEntity<Void> toggleCustomerBan(@PathVariable Long id) {
        adminService.toggleCustomerBan(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────────────────────────
    //  Seller Ban/Unban
    // ────────────────────────────────────────────────
    @PutMapping("/sellers/{id}/ban")
    public ResponseEntity<Void> toggleSellerBan(@PathVariable Long id) {
        adminService.toggleSellerBan(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────────────────────────
    //  Product Management
    // ────────────────────────────────────────────────
    @PutMapping("/products/{id}/cancel")
    public ResponseEntity<Void> cancelProduct(@PathVariable Long id) {
        productService.cancelProduct(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/{id}/uncancel")
    public ResponseEntity<Void> uncancelProduct(@PathVariable Long id) {
        productService.uncancelProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(adminService.getAllProductDTOs());
    }

    @DeleteMapping("/products/{id}/delete")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        adminService.deleteProductPermanently(id);
        return ResponseEntity.ok(Map.of("message", "Deletion success!"));
    }

    // ────────────────────────────────────────────────
    //  Orders
    // ────────────────────────────────────────────────
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{orderId}/status")
    public ResponseEntity<OrderDTO> getOrderStatus(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody OrderDTO orderDTO) {
        try {
            OrderStatus status = OrderStatus.valueOf(orderDTO.getStatus());
            orderService.updateStatus(orderId, status);
            return ResponseEntity.ok(Map.of("message", "Update success!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status value."));
        }
    }

    // ────────────────────────────────────────────────
    //  Refund Management
    // ────────────────────────────────────────────────
    /** Admin onaylı iade */
    @PostMapping("/orders/{orderId}/refund-approve")
    public ResponseEntity<RefundResponse> approveRefund(@PathVariable Long orderId) throws StripeException {
        RefundResponse resp = refundService.createRefund(orderId);
        return ResponseEntity.ok(resp);
    }

    /** Admin reddi */
    @PostMapping("/orders/{orderId}/refund-decline")
    public ResponseEntity<Void> declineRefund(@PathVariable Long orderId) {
        refundService.declineRefund(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category-count")
    public  ResponseEntity<List<CategoryCountProjection>> getCategoryCounts() {
        return ResponseEntity.ok(adminService.getProductCountByCategory());
    }
}