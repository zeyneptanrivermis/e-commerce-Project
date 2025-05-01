package com.example.ecommerce_api.entity.UserEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

//ekleme yapilabilr
@Entity
@Table(name = "address")
public class Address {

    // userda onetomany kurduk burada şart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String country = "Turkey"; // su an sadece turkiye icinde kargo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private City city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private District district;

    public Address() {}

    public Address(City city, District district) {
        this.city = city;
        this.district = district;
    }

    public String showAddressDetails() {
        return country + ", " + city + ", " + district;
    }

    // Getter - Setter
    public Long getAddressId() {
        return addressId;
    }

    public String getCountry() {
        return country;
    }

    public City getCity() {
        return city;
    }

    public District getDistrict() {
        return district;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && !user.getAddressList().contains(this)) {
            user.getAddressList().add(this);
        }
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public void setDistrict(District district) {
        this.district = district;
    }

    public Address addAddressToUser(Long userId, Address address) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAddressToUser'");
    }

    public Object getAddressesByUserId(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAddressesByUserId'");
    }
}
