package com.example.ecommerce_api.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


import com.example.ecommerce_api.entity.ProductEntity.Discount;
import com.example.ecommerce_api.entity.UserEntity.Customer;
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

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderItem> getItemList() { return itemList; }
    public void setItemList(List<OrderItem> itemList) { this.itemList = itemList; }

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

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
