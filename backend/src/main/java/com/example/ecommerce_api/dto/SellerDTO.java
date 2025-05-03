package com.example.ecommerce_api.dto;

public class SellerDTO {
    private Long id;
    private String name;
    private String email;

    public SellerDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public SellerDTO(SellerDTO seller) {
        if (seller != null) {
            this.id = seller.getId();
            this.name = seller.getName();
            this.email = seller.getEmail();
        }
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
}
