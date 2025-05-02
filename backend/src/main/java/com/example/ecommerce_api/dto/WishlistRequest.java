package com.example.ecommerce_api.dto;

//frontendden gelen productId bilgisisni karsilamak icin kullanilacaktir
public class WishlistRequest {
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
