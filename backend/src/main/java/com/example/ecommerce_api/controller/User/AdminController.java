package com.example.ecommerce_api.controller.User;

import com.example.ecommerce_api.dto.OrderDTO.OrderDTO;
import com.example.ecommerce_api.dto.ProductDTO.ProductDTO;
import com.example.ecommerce_api.dto.UserDTO.AdminUserDTO;
import com.example.ecommerce_api.dto.UserDTO.StatsDTO;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.CategoryCountProjection;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.services.Product.ProductService;
import com.example.ecommerce_api.services.User.AdminService;
import com.example.ecommerce_api.services.User.CustomerService;
import com.example.ecommerce_api.services.Order.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200") // Angular erişimi için
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    public AdminController(AdminService adminService, ProductRepository productRepository, OrderService orderService, CustomerService customerService, ProductService productService) {
        this.adminService = adminService;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.customerService = customerService;
        this.productService=productService;
    }

    // 🔐 Bu endpoint sadece ADMIN rolüne açık
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // --- STATISTICS endpoint'i ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<StatsDTO> getStats() {
        StatsDTO stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customers")
    public ResponseEntity<List<AdminUserDTO>> getAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomersDto());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sellers")
    public ResponseEntity<List<AdminUserDTO>> getAllSellers() {
        return ResponseEntity.ok(adminService.getAllSellersDto());
    }

    // ────────────────────────────────────────────────
    //  1) Müşteriyi yasakla / yasaklamayı kaldır
    // ────────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/customers/{id}/ban")
    public ResponseEntity<Void> toggleCustomerBan(@PathVariable Long id) {
        adminService.toggleCustomerBan(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────────────────────────
    //  2) Satıcıyı yasakla / yasaklamayı kaldır
    // ────────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/sellers/{id}/ban")
    public ResponseEntity<Void> toggleSellerBan(@PathVariable Long id) {
        adminService.toggleSellerBan(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────────────────────────
    //  3) Ürünü iptal et (soft-delete veya durum güncelle)
    // ────────────────────────────────────────────────
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/products/{id}/cancel")
    public ResponseEntity<Void> cancelProduct(@PathVariable Long id) {
        productService.cancelProduct(id);
        return ResponseEntity.ok().build();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/customers/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        customerService.updateCustomer(id, updatedCustomer);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(adminService.getAllProductDTOs());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/products/{id}/delete")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        adminService.deleteProductPermanently(id); // ← doğru servis
        return ResponseEntity.ok(Map.of("message", "Deletion success!"));
    }

    // ────────────────────────────────────────────────orders   

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> getOrderStatus(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
    
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody OrderDTO orderDTO) {
        try {
            OrderStatus status = OrderStatus.valueOf(orderDTO.getStatus()); // string → enum
            orderService.updateStatus(orderId, status);
            return ResponseEntity.ok(Map.of("message", "update success!"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Geçersiz status string
        }
    }
    @GetMapping("/category-count")
    public Map<String, Long> getProductCountByCategory() {
        return productRepository.countProductsGroupedByCategory()
                .stream()
                .collect(Collectors.toMap(CategoryCountProjection::getCategoryName, CategoryCountProjection::getCount));
    }

}