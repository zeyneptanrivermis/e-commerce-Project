package com.example.ecommerce_api.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Stock;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;


//bitti
@Entity
public class Seller extends User {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stock_address_id")
    private Address stockAddress;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Stock> sellingStocks = new ArrayList<>();

    public Address getStockAddress() {
        return stockAddress;
    }

    public void setStockAddress(Address stockAddress) {
        this.stockAddress = stockAddress;
    }

    public List<Stock> getSellingStocks() {
        return sellingStocks;
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
        if(this.sellingStocks.contains(product))
            this.sellingStocks.remove(product);
    }

}