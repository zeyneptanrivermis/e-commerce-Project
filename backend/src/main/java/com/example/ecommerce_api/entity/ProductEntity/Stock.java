package com.example.ecommerce_api.entity.ProductEntity;

import java.io.Console;
import java.lang.reflect.Constructor;
import java.time.LocalDate;

import org.springframework.context.annotation.Lazy;

import com.example.ecommerce_api.entity.UserEntity.Seller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;

//temizle
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
<<<<<<< Updated upstream
    private Seller store;
=======
    private Seller seller;
>>>>>>> Stashed changes

    private int quantity;
    private LocalDate restockedDate;

    public Stock() {}

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

<<<<<<< Updated upstream
    public Seller getStore() {
        return store;
=======
    public Seller getSeller() {
        return seller;
>>>>>>> Stashed changes
    }

    public LocalDate getRestockedDate() {
        return restockedDate;
    }

    public Long getStockId() {
        return stockId;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setRestockedDate(LocalDate restockedDate) {
        this.restockedDate = restockedDate;
    }
    public void setStock_id(Long stock_id) {
        this.stockId = stock_id;
    }
<<<<<<< Updated upstream
    public void setStore(Seller store) {
        this.store = store;
=======
    public void setSeller(Seller seller) {
        this.seller = seller;
>>>>>>> Stashed changes
    }
    @PrePersist
    protected void onCreate() {
        this.restockedDate = LocalDate.now();
    }

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

