package com.example.ecommerce_api.dto.UserDTO;

public class SellerRegisterDTO {

    private String name;
    private String surname;
    private String shopName;
    private String email;
    private String password;

    public SellerRegisterDTO() {}

    public SellerRegisterDTO(String name, String surname, String shopName, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.shopName = shopName;
        this.email = email;
        this.password = password;
    }

    // Getter ve Setter'lar

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
