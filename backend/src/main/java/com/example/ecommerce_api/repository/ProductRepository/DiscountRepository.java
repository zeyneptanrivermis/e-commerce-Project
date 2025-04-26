package com.example.ecommerce_api.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.ProductEntity.Discount;
import com.example.ecommerce_api.entity.ProductEntity.Product;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByProduct(Product product);
    Optional<Discount> findByDiscountCode(String discountCode);
}
