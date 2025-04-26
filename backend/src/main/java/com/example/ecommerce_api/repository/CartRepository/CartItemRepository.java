package com.example.ecommerce_api.repository.CartRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.CartEntity.CartItem;



public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart); // sepetin tüm ürünlerini getirir

    void deleteByCartAndProduct(Cart cart, com.example.ecommerce_api.entity.ProductEntity.Product product); // belirli ürünü siler
}
