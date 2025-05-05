package com.example.ecommerce_api.dto.UserDTO;

import java.util.List;

public class AdminUserDTO {
    private Long userId;
    private String name;
    private String email;
    private List<String> roles;
    private Boolean banned;

    public AdminUserDTO() {}
    public AdminUserDTO(Long userId, String name, String email, List<String> roles, Boolean banned) {
        this.userId = userId;
        this.name   = name;
        this.email  = email;
        this.roles  = roles;
        this.banned = banned;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public Boolean getBanned() {
        return banned;
    }
    public void setBanned(Boolean banned) {
        this.banned = banned;
    }
}
