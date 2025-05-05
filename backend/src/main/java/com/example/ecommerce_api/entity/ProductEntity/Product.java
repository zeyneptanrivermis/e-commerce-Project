package com.example.ecommerce_api.entity.ProductEntity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_api.dto.UserDTO.SellerDTO;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;

//BITMEDI
@Entity
@Table(name = "product")
public class Product {

    @Id//@Id: Bu alanın birincil anahtar olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @NotNull yazmadik cunku user degil veritabani id verecek
    @JsonProperty("id") //frontendde tutarli isim icin cevirme
    private Long productId;

    @NotBlank(message = "Product name cannot be blank.")
    @JsonProperty("name")
    private String productName;

    @Min(value = 0, message = "Price cannot be negative.")
    @NotNull
    private double price;

    @Size(min = 0, max = 500, message = "Description can have max 500 characters.")
    private String description;

    @Enumerated(EnumType.STRING)
    @JsonProperty("mainCategory")
    private Category category;

    @ElementCollection
    private List<String> sideCategories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Seller seller;

    private double shippingCost;

    @Min(value = 1, message = "Stock can be between 1-500.")
    @Max(value = 500, message = "Stock can be between 1-500.")
    private int stockCount;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;
    private double avgRating;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Discount> discounts = new ArrayList<>();

    @Column(length = 500)
    private String imageUrl;

    private Boolean cancelled = false;


    /*Product(String productName, double price, String description, Category mCategory){
        this.productName=productName;
        this.price=price;
        this.description=description;
    }*/

    // Getters and Setters

    public Category getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public Long getProductId() {
        return productId;
    }
    public String getProductName() {
        return productName;
    }
    public double getShippingCost() {
        return shippingCost;
    }
    public int getStockCount() {
        return stockCount;
    }
    public List<String> getSideCategories() {
        return sideCategories;
    }
    public List<Review> getReviews() {
        return reviews;
    }
    public double getAvgRating() {
        return avgRating;
    }
    public List<Discount> getDiscounts() {
        return discounts;
    }
    public Seller getSeller() {
        return seller;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setShippingCost(double shippingCost) {
        this.shippingCost = shippingCost;
    }
    public void setStockCount(int stockCount) {
            this.stockCount = stockCount;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public void setSideCategories(List<String> sideCategories) {
        this.sideCategories = sideCategories;
    }
    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
/* 
    */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product p = (Product) obj;
        return productId != null && productId.equals(p.productId);
    }

    @Override
    public int hashCode() {
        if (productId != null) {
            return productId.hashCode();
        } else {
            return 0;
        }
    }

    public Boolean getCancelled() {
        return cancelled;
    }
    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }
}

