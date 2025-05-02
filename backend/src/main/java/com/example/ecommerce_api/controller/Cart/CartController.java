package com.example.ecommerce_api.controller.Cart;

import com.example.ecommerce_api.dto.CartItemDTO;
import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;
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
    public ResponseEntity<String> addToCart(@RequestBody CartItemDTO request, Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        cartService.addToCart(customerId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Product added to cart successfully.");
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemDTO>> listCartItems(Principal principal) {
    Long customerId = getCustomerIdFromPrincipal(principal);
    List<CartItem> items = cartService.listCartItems(customerId);

    List<CartItemDTO> dtoList = items.stream().map(item -> {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(item.getCartItemId());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());

        ProductDTO productDto = new ProductDTO(
            item.getProduct().getProductId(),
            item.getProduct().getProductName(),
            item.getProduct().getPrice(),
            null // Seller bilgisi gerekiyorsa burada eklenebilir
        );

        dto.setProduct(productDto);
        return dto;
    }).toList();

    return ResponseEntity.ok(dtoList);
}


    @PutMapping("/update")
    public ResponseEntity<String> updateQuantity(@RequestParam Long productId,
                                             @RequestParam int quantity,
                                             Principal principal) {
    Long customerId = getCustomerIdFromPrincipal(principal);
    cartService.updateQuantity(customerId, productId, quantity);
    return ResponseEntity.ok("Quantity updated successfully.");
    }


    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long productId, 
                                                 Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        cartService.removeFromCart(customerId, productId);
        return ResponseEntity.ok("Product removed from cart successfully.");
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getCartTotal(Principal principal) {
        Long customerId = getCustomerIdFromPrincipal(principal);
        double total = cartService.getCartTotal(customerId);
        return ResponseEntity.ok(total);
    }

    // Yardımcı method: Principal'dan kullanıcı ID'si çıkarma
    private Long getCustomerIdFromPrincipal(Principal principal) {
        String email = principal.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getUserId();
    }

    @Autowired
    private UserRepository userRepository;
}

