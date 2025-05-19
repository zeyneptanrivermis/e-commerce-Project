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

    List<Product> findBySellerUserId(Long sellerId);

    // İstersen stock kontrolü yapabilirsin
    List<Product> findByStockCountGreaterThan(int stockCount);

    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByProductNameContaining(String keyword);


    long countBySeller(Seller seller);

    List<Product> findAllBySeller(Seller seller);

    @Query("SELECT p FROM Product p WHERE p.category = :category OR :category IN elements(p.sideCategories)")
    List<Product> findByMainOrSideCategory(@Param("category") String category);

    List<Product> findByCategory(Category category);

    @Query("SELECT p.category AS categoryName, COUNT(p) AS count FROM Product p GROUP BY p.category")
    List<CategoryCountProjection> countProductsGroupedByCategory();


@Query(value = """
    SELECT
        p.product_name AS productName,
        SUM(oi.quantity) AS sales,
        SUM(oi.quantity * oi.price) AS revenue
    FROM order_item oi
    JOIN product p ON oi.product_id = p.product_id
    JOIN seller s ON p.user_id = s.user_id
    JOIN user u ON s.user_id = u.user_id
    WHERE u.email = :email
    GROUP BY p.product_name
    ORDER BY sales DESC
    LIMIT 5
""", nativeQuery = true)
List<TopSellingProductProjection> findTopSellingProductsBySellerEmail(@Param("email") String email);

}


