package com.example.ecommerce_api.dto.UserDTO;

public class SellerDTO {
    private Long id;
    private String name;
    private String email;

        // Dashboard bilgileri
        private long totalProducts;
        private long totalOrders;


    public SellerDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public SellerDTO() {}

    public SellerDTO(Long id, String name, String email, long totalProducts, long totalOrders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
    }

    // Getter ve Setter'lar
    public String getEmail() {
        return email;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getTotalOrders() {
        return totalOrders;
    }
    public long getTotalProducts() {
        return totalProducts;
    }
    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }
    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }
}
