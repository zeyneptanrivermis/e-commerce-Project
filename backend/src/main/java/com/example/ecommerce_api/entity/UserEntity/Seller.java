package com.example.ecommerce_api.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Stock;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;



//bitti, değiştirildi
@Entity
@Table(name = "seller")
public class Seller extends User {

    private Boolean banned = false;

    private String shopName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_address_id")
    private Address stockAddress;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Stock> sellingStocks = new ArrayList<>();

    public Boolean getBanned() {
        return banned;
    }
    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Address getStockAddress() {
        return stockAddress;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    
    public void setStockAddress(Address stockAddress) {
        this.stockAddress = stockAddress;
    }

    public List<Stock> getSellingStocks() {
        return sellingStocks;
    }

    public void setSellingStocks(List<Stock> sellingStocks) {
        this.sellingStocks = sellingStocks;
    }

    public void restock(Product product, int number) {
        for (Stock s : sellingStocks) {
            if (s.getProduct().equals(product)) {
                s.setQuantity(s.getQuantity() + number);
                s.setRestockedDate(java.time.LocalDate.now());
                return;
            }
        }

    }

    public void removeProduct(Product product){
        sellingStocks.removeIf(stock -> stock.getProduct().equals(product));
    }    

}