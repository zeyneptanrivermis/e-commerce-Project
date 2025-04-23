package com.example.entity.ProductEntity;

import java.io.Console;
import java.lang.reflect.Constructor;
import java.time.LocalDate;

import com.example.entity.UserEntity.Seller;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//temizle
@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stock_id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller store;

    private int quantity;
    private LocalDate restockedDate;

    public Stock() {}

    public Stock(Product product, int quantity, Seller seller) {
        this.product = product;
        this.restockedDate = LocalDate.now();
        this.quantity = quantity;
        this.store = seller;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Seller getStore() {
        return store;
    }

    public LocalDate getRestockedDate() {
        return restockedDate;
    }

    public Long getStockId() {
        return stock_id;
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
        this.stock_id = stock_id;
    }
    public void setStore(Seller store) {
        this.store = store;
    }
}

