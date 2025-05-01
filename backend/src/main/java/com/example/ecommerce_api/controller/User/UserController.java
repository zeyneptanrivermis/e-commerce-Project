package com.example.ecommerce_api.controller.User;

import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.dto.UserDTO;
import com.example.ecommerce_api.dto.WishlistRequest;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;
import com.example.ecommerce_api.security.CustomerDetails;
import com.example.ecommerce_api.security.JwtUtil;
import com.example.ecommerce_api.services.User.CustomerService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;

    public UserController(
        UserRepository userRepository,
        CustomerRepository customerRepository,
        JwtUtil jwtUtil,
        CustomerService customerService
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.jwtUtil = jwtUtil;
        this.customerService = customerService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Eksik Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token geçersiz");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        return ResponseEntity.ok(new UserDTO(user));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User updatedUser) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Eksik Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Token geçersiz");
        }

        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("Kullanıcı bulunamadı");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

        userRepository.save(existingUser);

        return ResponseEntity.ok("Kullanıcı başarıyla güncellendi");
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishlist(Authentication authentication) {
        CustomerDetails userDetails = (CustomerDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));

        List<ProductDTO> wishlist = customer.getWishlist().stream()
            .map(ProductDTO::new)
            .toList();

        return ResponseEntity.ok(wishlist);
    }
    @PostMapping("/wishlist")
    public ResponseEntity<?> addToWishlist(
            Authentication authentication,
            @RequestBody WishlistRequest request) {

        String email = ((CustomerDetails) authentication.getPrincipal()).getUsername();

        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));

        customerService.addProductToWishlist(customer, request.getProductId());

        return ResponseEntity.ok(Map.of("message", "Ürün wishlist'te eklendi"));
    }

    @DeleteMapping("/wishlist")
    public ResponseEntity<?> removeFromWishlist(Authentication authentication, @RequestBody WishlistRequest request) {
        String email = ((CustomerDetails) authentication.getPrincipal()).getUsername();
    
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Müşteri bulunamadı"));
    
        customerService.removeProductFromWishlist(customer, request.getProductId());
    
        return ResponseEntity.ok(Map.of("message", "Ürün wishlist'ten silindi"));
    }
    

}
