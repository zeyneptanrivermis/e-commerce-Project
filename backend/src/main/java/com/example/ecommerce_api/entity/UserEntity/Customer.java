package com.example.ecommerce_api.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;

import com.example.ecommerce_api.entity.ProductEntity.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

//bitti
@Entity
public class Customer extends User {

    @ManyToMany
    @JoinTable(
        name = "wishlist",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> wishlist = new HashSet<>();

    public Set<Product> getWishlist() {
        return wishlist;
    }

    public void setWishlist(Set<Product> wishlist) {
        this.wishlist = wishlist;
    }

    public void addToWishlist(Product product) {
        this.wishlist.add(product);
    }

    public void removeFromWishlist(Product product) {
        this.wishlist.remove(product);
    }
}



