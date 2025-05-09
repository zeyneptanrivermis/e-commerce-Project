package com.example.ecommerce_api.controller.User;

import com.example.ecommerce_api.dto.ProductDTO.ProductDTO;
import com.example.ecommerce_api.dto.UserDTO.AdminUserDTO;
import com.example.ecommerce_api.dto.UserDTO.StatsDTO;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.services.Product.ProductService;
import com.example.ecommerce_api.services.User.AdminService;
import com.example.ecommerce_api.services.User.CustomerService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200") // Angular erişimi için
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;
    private final ProductService productService;

    public AdminController(AdminService adminService, CustomerService customerService, ProductService productService) {
        this.adminService = adminService;
        this.customerService = customerService;
        this.productService=productService;
    }

    // 🔐 Bu endpoint sadece ADMIN rolüne açık
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // --- STATISTICS endpoint’i ---
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
    
}