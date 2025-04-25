package com.example.ecommerce_api.entity.ProductEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;

import com.example.ecommerce_api.entity.UserEntity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


//Bitmedi
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Min(0)
    @Max(5)
    private int rating;

    @Size(min = 3, max = 150, message = "Your comment must be between 3 and 150 characters.")
    private String comment;

    private boolean isEdited=false;

    // Getters & Setters
    public String getComment() {
        return comment;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Product getProduct() {
        return product;
    }
    public int getRating() {
        return rating;
    }
    public Long getReviewId() {
        return reviewId;
    }
    public boolean isEdited() {
        return isEdited;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }



    //muhtemelen silinecek
    public void editComment(String newComment){
        if (newComment.length() >= 3 && newComment.length() <= 150) {
            this.comment = newComment;
            this.isEdited = true;
        } else {
            throw new IllegalArgumentException("Comment must be between 3 and 150 characters.");
        }
    }

    public void deleteComment(){
        this.product.getReviews().remove(this);
        this.product.updateAvgRating();
    }
}
