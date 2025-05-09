package com.example.ecommerce_api.dto.ProductDTO;

import java.util.List;

import com.example.ecommerce_api.entity.ProductEntity.Category;

public class ProductRequestDto {
    private String name;
    private String description;
    private double price;
    private Category mainCategory;
    private List<String> sideCategories;
    private double shippingCost;
    private int stockCount;
    private boolean cancelled;

    // GETTER ve SETTER'lar:
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Category getMainCategory() { return mainCategory; }
    public void setMainCategory(Category mainCategory) { this.mainCategory = mainCategory; }

    public List<String> getSideCategories() { return sideCategories; }
    public void setSideCategories(List<String> sideCategories) { this.sideCategories = sideCategories; }

    public double getShippingCost() { return shippingCost; }
    public void setShippingCost(double shippingCost) { this.shippingCost = shippingCost; }

    public int getStockCount() { return stockCount; }
    public void setStockCount(int stockCount) { this.stockCount = stockCount; }

    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}
