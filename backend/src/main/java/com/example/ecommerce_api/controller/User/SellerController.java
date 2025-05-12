package com.example.ecommerce_api.controller.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.example.ecommerce_api.dto.ProductDTO.ProductMapper;
import com.example.ecommerce_api.dto.ProductDTO.ProductRequestDto;
import com.example.ecommerce_api.dto.UserDTO.LoginRequest;
import com.example.ecommerce_api.dto.featuresDTO.AuthResponse;
import com.example.ecommerce_api.dto.UserDTO.SellerRegisterDTO;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Role;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.UserRepositories.RoleRepository;
import com.example.ecommerce_api.repository.UserRepositories.SellerRepository;
import com.example.ecommerce_api.services.Product.ProductService;
import com.example.ecommerce_api.services.User.SellerService;

@RestController
@RequestMapping("/api/seller/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class SellerController {

    @Autowired private SellerService sellerService;
    @Autowired private SellerRepository sellerRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private ProductService productService;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SellerRegisterDTO dto) {
        if (sellerRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email zaten kayıtlı");
        }

        Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setSurname(dto.getSurname());
        seller.setEmail(dto.getEmail());
        seller.setPassword(passwordEncoder.encode(dto.getPassword()));
        seller.setShopName(dto.getShopName());

        Role sellerRole = roleRepository.findByName("ROLE_SELLER")
            .orElseGet(() -> roleRepository.save(new Role("ROLE_SELLER")));

        seller.setRoles(Set.of(sellerRole));
        sellerRepository.save(seller);

        return ResponseEntity.ok("Satıcı başarıyla kaydedildi");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = sellerService.login(request);
            return ResponseEntity.ok(new AuthResponse(token, null));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Giriş başarısız: " + e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getSellerProducts(Authentication authentication) {
        String sellerEmail = authentication.getName();
        List<Product> products = productService.getProductsBySellerEmail(sellerEmail);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto dto, Authentication auth) {
        String email = auth.getName();
        Product product = ProductMapper.fromDto(dto);
        Product saved = productService.saveProductForSeller(product, email);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getSellerDashboard(Authentication auth) {
        String email = auth.getName();

        // Örnek metrikler - burada gerçek servislere bağlanarak veri getirin
        long totalProducts = productService.getProductsBySellerEmail(email).size();
        long totalOrders = 12;  // Örn: sipariş servisi ile satıcının sipariş sayısı
        double totalSales = 2599.90;  // Örn: toplam satış tutarı

        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalProducts", totalProducts);
        dashboardData.put("totalOrders", totalOrders);
        dashboardData.put("totalSales", totalSales);

        return ResponseEntity.ok(dashboardData);
    }
}