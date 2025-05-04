package com.example.ecommerce_api.services.Cart;

import java.util.List;

import com.example.ecommerce_api.dto.OrderDTO.CartItemDTO;
import com.example.ecommerce_api.entity.CartEntity.CartItem;

public interface CartService {
    void addToCart(Long customerId, Long productId, int quantity);
    void removeFromCart(Long customerId, Long productId);
    List<CartItem> listCartItems(Long customerId); 
    double getCartTotal(Long customerId);
    void updateQuantity(Long customerId, Long productId, int quantity);
}

