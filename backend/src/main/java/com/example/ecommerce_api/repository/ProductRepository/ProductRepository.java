package com.example.ecommerce_api.repository.ProductRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import com.example.ecommerce_api.dto.ProductDTO.CategoryProductCountDTO;
import com.example.ecommerce_api.entity.ProductEntity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Seller;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findAll(Pageable pageable);
    
    @Query("SELECT p FROM Product p")
    List<Product> getAllProductsRaw();

    // İstediğin ürünü satıcıya göre getirmek istersen
    List<Product> findBySellerUserId(Long sellerId);

    // İstersen stock kontrolü yapabilirsin
    List<Product> findByStockCountGreaterThan(int stockCount);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByProductNameContaining(String keyword);


    long countBySeller(Seller seller);

    List<Product> findAllBySeller(Seller seller);

    // ProductRepository.java
    // Removed duplicate method definition to resolve the error

    @Query("SELECT p FROM Product p WHERE p.category = :category OR :category IN elements(p.sideCategories)")
    List<Product> findByMainOrSideCategory(@Param("category") String category);

    List<Product> findByCategory(Category category);

@Query("SELECT p.category AS categoryName, COUNT(p) AS count FROM Product p GROUP BY p.category")
List<CategoryCountProjection> countProductsGroupedByCategory();


}

