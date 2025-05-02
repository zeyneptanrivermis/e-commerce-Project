package com.example.ecommerce_api.dto;


import java.util.List;

import com.example.ecommerce_api.entity.ProductEntity.Category;
import com.example.ecommerce_api.entity.ProductEntity.Product;

public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String description;
    private double avgRating;
    private double shippingCost;
    private Category mainCategory;
    private int stockCount;

    private SellerDTO seller;
    private List<ReviewDTO> reviews;

    public ProductDTO(Long id, String name, double price, SellerDTO seller, String description,
                      double avgRating, double shippingCost,
                      Category mainCategory, int stockCount, List<ReviewDTO> reviews) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description= description;
        this.seller = seller;
        this.avgRating = avgRating;
        this.shippingCost = shippingCost;
        this.mainCategory = mainCategory;
        this.stockCount = stockCount;
        this.reviews=reviews;
    }
    
    public ProductDTO(Product product) {
        this.id = product.getProductId();
        this.name = product.getProductName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.avgRating = product.getAvgRating();
        this.shippingCost = product.getShippingCost();
        this.mainCategory = product.getCategory();
        this.stockCount = product.getStockCount();

        this.seller = new SellerDTO(
            product.getSeller().getUserId(),
            product.getSeller().getName(),
            product.getSeller().getEmail()
        );

        this.reviews = product.getReviews().stream()
            .map(ReviewDTO::new)
            .toList();
    }

    // Getter ve Setter'lar

    public ProductDTO(Long productId, String productName, double price2, Object object) {
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getSellerDTOId() {
        return seller.getId();
    }

    public void setSeller(SellerDTO seller) {
        this.seller = seller;
    }
    public double getAvgRating() {
        return avgRating;
    }
    public Category getMainCategory() {
        return mainCategory;
    }
    public SellerDTO getSeller() {
        return seller;
    }
    public double getShippingCost() {
        return shippingCost;
    }
    public int getStockCount() {
        return stockCount;
    }
    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
    public void setMainCategory(Category mainCategory) {
        this.mainCategory = mainCategory;
    }

    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }
    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<ReviewDTO> getReviews() {
        return reviews;
    }
    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
