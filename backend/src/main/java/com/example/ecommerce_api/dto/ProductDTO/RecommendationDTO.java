package com.example.ecommerce_api.dto.ProductDTO;

public class RecommendationDTO {
    private Long productId;
    private double score;

    public  RecommendationDTO(Long productId, double score) {
        this.productId = productId;
        this.score = score;
    }

    public Long getProductId() {
        return productId;
    }

    public double getScore() {
        return score;
    }
}
