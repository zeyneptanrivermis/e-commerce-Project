package com.example.ecommerce_api.dto;

import java.time.LocalDate;

import com.example.ecommerce_api.entity.UserEntity.Gender;
import com.example.ecommerce_api.entity.UserEntity.User;

public class UserDTO {
    private Long userId;
    private String name;
    private String surname;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.dateOfBirth = user.getDateOfBirth();
    }

    // getter-setter
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public String getEmail() {
        return email;
    }public Gender getGender() {
        return gender;
    }
    public String getName() {
        return name;
    }public String getSurname() {
        return surname;
    }
    public Long getUserId() {
        return userId;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}