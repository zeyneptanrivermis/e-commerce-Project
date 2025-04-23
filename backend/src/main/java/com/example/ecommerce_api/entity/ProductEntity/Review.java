package com.example.ecommerce_api.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;

import com.example.ecommerce_api.entity.UserEntity.Customer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

//Bitmedi
public class Review {
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    private Customer customer;
    private int rating;

}
