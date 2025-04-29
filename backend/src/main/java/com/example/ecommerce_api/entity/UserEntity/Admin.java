package com.example.ecommerce_api.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;



//bitmedi simdilik ne yapacagimi bilmiyorum
@Entity
@Table(name = "admin")
public class Admin extends User {

    private String permission;

    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
}
