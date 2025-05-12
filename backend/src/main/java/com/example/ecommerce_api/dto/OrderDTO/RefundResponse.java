package com.example.ecommerce_api.dto.OrderDTO;

public class RefundResponse {
    private String refundId;
    private String status;

    public RefundResponse(String refundId, String status) {
        this.refundId = refundId;
        this.status = status;
    }

    public String getRefundId() {
        return refundId;
    }

    public String getStatus() {
        return status;
    }
}
