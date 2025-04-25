package com.example.ecommerce_api.entity.CartEntity;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

public class CartItem {
    //baslanmadi
    @Id//@Id: Bu alanın birincil anahtar olduğunu belirtir.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @OneToOne
    private Product product;

}
