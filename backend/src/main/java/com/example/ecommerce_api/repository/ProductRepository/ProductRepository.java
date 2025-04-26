package com.example.ecommerce_api.repository.ProductRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.ProductEntity.Category;
import com.example.ecommerce_api.entity.ProductEntity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // İstediğin ürünü kategoriye göre getirmek istersen
    List<Product> findByCategory(Category category);

    // İstediğin ürünü satıcıya göre getirmek istersen
    List<Product> findBySellerId(Long sellerId);

    // İstersen stock kontrolü yapabilirsin
    List<Product> findByStockCountGreaterThan(int stockCount);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByProductNameContaining(String keyword);
}
