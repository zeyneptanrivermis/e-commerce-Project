package com.example.ecommerce_api.repository.ProductRepository;

public interface TopSellingProductProjection {
    String getProductName();
    Integer getSales();
    Double getRevenue();
}