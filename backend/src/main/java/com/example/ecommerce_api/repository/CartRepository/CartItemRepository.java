package com.example.ecommerce_api.repository.CartRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.entity.ProductEntity.Product;



public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Belirli bir sepetteki ürünleri getir
    List<CartItem> findByCart(Cart cart);

    // Sepette belirli bir ürün var mı?
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

    // Belirli bir ürünü silme
    void deleteByCartAndProduct(Cart cart, Product product);

    int countByCart(Cart cart);
}
