package com.example.ecommerce_api.dto;

public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String seller;

    public ProductDTO(Long id, String name, double price, String seller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.seller = seller;
    }

    // Getter ve Setter'lar

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSellerEmail() {
        return seller;
    }

    public void setSellerEmail(String sellerEmail) {
        this.seller = sellerEmail;
    }
}
