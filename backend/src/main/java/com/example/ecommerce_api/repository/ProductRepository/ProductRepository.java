package com.example.ecommerce_api.repository.ProductRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.ProductEntity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);
    
    // İstediğin ürünü satıcıya göre getirmek istersen
    List<Product> findBySellerUserId(Long sellerId);

    // İstersen stock kontrolü yapabilirsin
    List<Product> findByStockCountGreaterThan(int stockCount);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByProductNameContaining(String keyword);

}
