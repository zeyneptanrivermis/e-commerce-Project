package com.example.ecommerce_api.dto.ProductDTO;

import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.entity.ProductEntity.Product;

public class ProductMapper {
    public static Product fromDto(ProductRequestDto dto) {
        Product p = new Product();
        p.setProductName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setPrice(dto.getPrice());
        p.setCategory(dto.getMainCategory());
        p.setSideCategories(dto.getSideCategories());
        p.setShippingCost(dto.getShippingCost());
        p.setStockCount(dto.getStockCount());
        p.setCancelled(dto.isCancelled());
        return p;
    }
}