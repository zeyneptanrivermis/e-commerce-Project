package com.example.ecommerce_api.entity.ProductEntity;

import com.fasterxml.jackson.annotation.JsonFormat;

//enum bitti
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Category {
    CLOTHING,
    MAKEUP,
    ELECTRONICS,
    PET_SUPPLIES,
    HOME_AND_KITCHEN,
    TOYS_AND_GAMES,
    SPORTS_AND_OUTDOOR,
    HOBBIES
}
