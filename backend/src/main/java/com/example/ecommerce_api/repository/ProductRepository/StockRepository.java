package com.example.ecommerce_api.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Stock;
import com.example.ecommerce_api.entity.UserEntity.Seller;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProductAndSeller(Product product, Seller seller);
    List<Stock> findBySeller(Seller seller);
}
