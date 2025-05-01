package com.example.ecommerce_api.dto;

import com.example.ecommerce_api.entity.ProductEntity.Review;

public class ReviewDTO {
    private int rating;
    private String comment;
    private boolean edited;
    private String customerName;
    private String customerSurname;

    public ReviewDTO(Review review) {
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.edited = review.isEdited();
        this.customerName = review.getCustomer().getName(); // veya email
        this.customerSurname = review.getCustomer().getSurname();
    }

    // Getter'lar
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public boolean isEdited() { return edited; }
    public String getCustomerName() { return customerName; }
    public String getCustomerSurname() {
        return customerSurname;
    }
}
