package com.example.ecommerce_api.entity.OrderEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


import com.example.ecommerce_api.entity.UserEntity.Address;

// incele, gözden geçir
@Entity
@Table(name = "shipping")
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shippingId;

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    private String carrier;
    private String trackingNumber;
    private String status;

    @OneToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    public Long getShippingId() { return shippingId; }
    public void setShippingId(Long shippingId) { this.shippingId = shippingId; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getTracking() {
        return trackingNumber;
    }
}
