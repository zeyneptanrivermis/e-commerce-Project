
package com.example.ecommerce_api.dto.OrderDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.example.ecommerce_api.entity.OrderEntity.OrderItem;

public class OrderDTO {
    private Long orderId;
    private double totalWithDiscount;
    private double totalWithoutDiscount;
    private String status;
    private LocalDate paymentDate; // ← burası önemli
    private List<OrderItemDTO> itemList;
    private PaymentDTO paymentInfo;

    public List<OrderItemDTO> getItemList() {
        return itemList;
    }public Long getOrderId() {
        return orderId;
    }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }public String getStatus() {
        return status;
    }
    public double getTotalWithDiscount() {
        return totalWithDiscount;
    }
    public double getTotalWithoutDiscount() {
        return totalWithoutDiscount;
    }

    public void setItemList(List<OrderItemDTO> itemList) {
        this.itemList = itemList;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setTotalWithDiscount(double totalWithDiscount) {
        this.totalWithDiscount = totalWithDiscount;
    }
    public void setTotalWithoutDiscount(double totalWithoutDiscount) {
        this.totalWithoutDiscount = totalWithoutDiscount;
    }
    public PaymentDTO getPaymentInfo() {
        return paymentInfo;
    }
    public void setPaymentInfo(PaymentDTO paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
