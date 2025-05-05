package com.example.ecommerce_api.dto.UserDTO;

public class StatsDTO {
    private long userCount;
    private long productCount;
    private long pendingOrders;

    public StatsDTO(long userCount, long productCount, long pendingOrders) {
        this.userCount = userCount;
        this.productCount = productCount;
        this.pendingOrders = pendingOrders;
    }

    // getters & setters
    public long getUserCount() { return userCount; }
    public void setUserCount(long userCount) { this.userCount = userCount; }
    public long getProductCount() { return productCount; }
    public void setProductCount(long productCount) { this.productCount = productCount; }
    public long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }
}
