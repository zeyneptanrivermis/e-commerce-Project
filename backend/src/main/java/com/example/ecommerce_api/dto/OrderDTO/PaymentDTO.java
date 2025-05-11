package com.example.ecommerce_api.dto.OrderDTO;

import java.time.LocalDate;
import com.example.ecommerce_api.entity.OrderEntity.Payment;

public class PaymentDTO {
    private Long orderId;
    private Long customerId;
    private double amount;
    private String status;
    private LocalDate paymentDate;
    private String cardholder;
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;

    // Getter - Setter

    public double getAmount() {
        return amount;
    }
    public String getCardNumber() {
        return cardNumber;
    }
    public String getCardholder() {
        return cardholder;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public String getCvv() {
        return cvv;
    }
    public String getExpiryMonth() {
        return expiryMonth;
    }
    public String getExpiryYear() {
        return expiryYear;
    }
    public Long getOrderId() {
        return orderId;
    }
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    public String getStatus() {
        return status;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
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


    public static PaymentDTO fromEntity(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setOrderId(payment.getOrder().getOrderId());
        dto.setCustomerId(payment.getCustomer().getUserId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setCardholder("**** " + payment.getCustomer().getName()); // örnek maskeleme
        return dto;
    }
}
