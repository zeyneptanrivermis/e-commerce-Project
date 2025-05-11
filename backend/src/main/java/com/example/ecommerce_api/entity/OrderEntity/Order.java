package com.example.ecommerce_api.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import com.example.ecommerce_api.entity.ProductEntity.Discount;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

// incele, gözden geçir
@Entity
@Table(name = "`order`") // ← dikkat: ters tırnak (backtick) kullanıyoruz
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> itemList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrderStatus status = OrderStatus.PENDING;  // default PENDING

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    // — Yeni eklenecek alan —
    @Column(name = "payment_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;
    
    @Column(name = "paid_amount")
    private Long paidAmount;


    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderItem> getItemList() { return itemList; }
    public void setItemList(List<OrderItem> itemList) { this.itemList = itemList; }

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }
    public String getPaymentIntentId() {
        return paymentIntentId;
    }
    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }
    public void setPaymentIntentId(String paymentIntentId) {
        this.paymentIntentId = paymentIntentId;
    }

    @JsonProperty
    public double getOrderTotalWithoutDiscount() {
        return itemList.stream()
                .mapToDouble(OrderItem::getOrderItemTotal)
                .sum();
    }

    @JsonProperty
    public double getOrderTotalWithDiscount() {
        double total = getOrderTotalWithoutDiscount();
        if (discount != null) {
            total -= total * (discount.getPercentage() / 100.0);
        }
        return total;
    }
}
