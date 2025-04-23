package com.example.entity.ProductEntity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.entity.UserEntity.Seller;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.*;

//BITMEDI
@Entity
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
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private double shippingCost;

    @Min(value = 1, message = "Stock can be between 1-500.")
    @Max(value = 500, message = "Stock can be between 1-500.")
    private int stockCount;

    private List<Review> reviews;
    private double avgRating;

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
    public void setCategory(Category category) {
        this.category = category;
    }
    public List<String> getSideCategories() {
        return sideCategories;
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
    public void setSideCategories(List<String> sideCategories) {
        this.sideCategories = sideCategories;
    }

}
