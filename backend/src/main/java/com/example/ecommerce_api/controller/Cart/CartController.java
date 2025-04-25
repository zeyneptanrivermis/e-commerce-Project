package com.example.ecommerce_api.controller.Cart;

import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.repository.UserRepository.UserRepository;
import com.example.ecommerce_api.services.Cart.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId, 
                                            @RequestParam int quantity, 
                                            Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        cartService.addToCart(customerId, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long productId, 
                                                 Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        cartService.removeFromCart(customerId, productId);
        return ResponseEntity.ok("Product removed from cart successfully.");
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> listCartItems(Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        List<CartItem> items = cartService.listCartItems(customerId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        double total = cartService.getCartTotal(customerId);
        return ResponseEntity.ok(total);
    }

    // Yardımcı method: Principal'dan kullanıcı ID'si çıkarma
    private Long getCustomerIdFromPrincipal(Principal principal) {
        // Burada principal.getName() ile email geliyor. 
        // userRepository.findByEmail() ile User bulunacak ve ID alınacak.
        String email = principal.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }

    @Autowired
    private UserRepository userRepository;
}

