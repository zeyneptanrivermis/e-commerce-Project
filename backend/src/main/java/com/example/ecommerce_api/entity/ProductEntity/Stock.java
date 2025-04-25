package com.example.ecommerce_api.entity.ProductEntity;

import java.time.LocalDate;

import com.example.ecommerce_api.entity.UserEntity.Seller;
import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private int quantity;
    private LocalDate restockedDate;

    public Stock() {}

    @PrePersist
    protected void onCreate() {
        this.restockedDate = LocalDate.now();
    }

    // Getters & Setters
    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getRestockedDate() {
        return restockedDate;
    }

    public void setRestockedDate(LocalDate restockedDate) {
        this.restockedDate = restockedDate;
    }

    // equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return stockId != null && stockId.equals(stock.stockId);
    }

    @Override
    public int hashCode() {
        return stockId != null ? stockId.hashCode() : 0;
    }
}
