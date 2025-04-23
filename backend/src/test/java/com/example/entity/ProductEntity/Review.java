package com.example.entity.ProductEntity;
import com.example.entity.UserEntity.Customer;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

//Bitmedi
public class Review {
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    private Customer customer;
    private int rating;

}
