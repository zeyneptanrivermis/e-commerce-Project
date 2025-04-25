package com.example.ecommerce_api.repository.ProductRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.ProductEntity.Product;

//baslanmadi
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
