package com.example.ecommerce_api.entity.UserEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.example.ecommerce_api.entity.ProductEntity.Product;

import jakarta.persistence.*;



//bitti
@Entity
@Table(name = "customer")
public class Customer extends User {

    private String wishListId = UUID.randomUUID().toString();

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
        if (!wishlist.contains(product)) {
            wishlist.add(product);
        }
    }

    public void removeFromWishlist(Product product) {
        this.wishlist.remove(product);
    }
}