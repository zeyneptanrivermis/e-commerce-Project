package com.example.ecommerce_api.entity.CartEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_api.entity.UserEntity.Customer;

// incele, gözden geçir
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItemSet = new ArrayList<>();

    // Getters & Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getCartItemSet() {
        return cartItemSet;
    }

    public void setCartItemSet(List<CartItem> cartItemSet) {
        this.cartItemSet = cartItemSet;
    }

    public void addToCart(CartItem item) {
        item.setCart(this);
        this.cartItemSet.add(item);
    }

    public void removeFromCart(CartItem item) {
        this.cartItemSet.remove(item);
        item.setCart(null);
    }

    public double getTotalPrice() {
        return cartItemSet.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
