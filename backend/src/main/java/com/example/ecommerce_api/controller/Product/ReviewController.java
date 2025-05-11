package com.example.ecommerce_api.controller.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce_api.dto.ProductDTO.ReviewDTO;
import com.example.ecommerce_api.dto.ProductDTO.ReviewRequestDTO;
import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.security.CustomerDetails;
import com.example.ecommerce_api.services.Product.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        List<ReviewDTO> dtos = reviews.stream()
                                      .map(ReviewDTO::new)
                                      .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(new ReviewDTO(review));
    }

    @GetMapping("/can-review/{productId}")
    public ResponseEntity<Boolean> canReview(
            @PathVariable Long productId,
            @AuthenticationPrincipal CustomerDetails userDetails
    ) {
        Long customerId = userDetails.getCustomer().getUserId();
        boolean allowed = reviewService.canReview(customerId, productId);
        return ResponseEntity.ok(allowed);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @RequestBody @Valid ReviewRequestDTO dto,
            @AuthenticationPrincipal CustomerDetails userDetails
    ) {
        Long customerId = userDetails.getCustomer().getUserId();
        // ↓ addReview → createReview olarak düzeltildi
        Review created = reviewService.createReview(customerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new ReviewDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequestDTO dto,
            @AuthenticationPrincipal CustomerDetails userDetails
    ) {
        Long customerId = userDetails.getCustomer().getUserId();
        Review updated = reviewService.updateReview(id, customerId, dto);
        return ResponseEntity.ok(new ReviewDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomerDetails userDetails
    ) {
        Long customerId = userDetails.getCustomer().getUserId();
        reviewService.deleteReview(id, customerId);
        return ResponseEntity.noContent().build();
    }
}
