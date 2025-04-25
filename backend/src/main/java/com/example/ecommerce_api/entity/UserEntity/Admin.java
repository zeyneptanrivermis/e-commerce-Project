package com.example.ecommerce_api.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


//bitmedi simdilik ne yapacagimi bilmiyorum

public class Admin extends User {
    private String permission;

    public String getPermission() {
        return permission;
    }
}
