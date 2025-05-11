package com.example.ecommerce_api.services.Order;

public class ShipmentStatusDTO {
    private Long orderId;
    private String status;

    public ShipmentStatusDTO(Long orderId, String status) {
        this.orderId = orderId;
        this.status  = status;
    }

    public Long getOrderId() { return orderId; }
    public String getStatus() { return status; }
}