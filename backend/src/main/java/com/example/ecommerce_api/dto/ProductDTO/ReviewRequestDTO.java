package com.example.ecommerce_api.dto.ProductDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequestDTO {
    @NotNull
    private Long productId;

    @Min(1) @Max(5)
    private int rating;

    @Size(min = 1, max = 1000)
    private String comment;

    // Getter & Setter
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
