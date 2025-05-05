package com.example.ecommerce_api.controller.User;

import com.example.ecommerce_api.dto.UserDTO.AdminUserDTO;
import com.example.ecommerce_api.dto.UserDTO.StatsDTO;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.services.User.AdminService;
import com.example.ecommerce_api.services.User.CustomerService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200") // Angular erişimi için
public class AdminController {

    private final AdminService adminService;
    private final CustomerService customerService;
    
    public AdminController(AdminService adminService, CustomerService customerService) {
        this.adminService = adminService;
        this.customerService = customerService;
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
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> cancelProduct(@PathVariable Long id) {
        adminService.cancelProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/customers/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        customerService.updateCustomer(id, updatedCustomer);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(adminService.getAllProducts());
    }

}