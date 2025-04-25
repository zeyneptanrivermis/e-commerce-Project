package com.example.ecommerce_api.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Stock;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;



//bitti, değiştirildi
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