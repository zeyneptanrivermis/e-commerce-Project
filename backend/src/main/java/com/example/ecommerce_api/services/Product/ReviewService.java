package com.example.ecommerce_api.services.Product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.ProductRepository.ReviewRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;
    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, 
    ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.productService=productService;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + id));
    }

    public Review createReview(Review review) {
        Review savedReview = reviewRepository.save(review);

        // Ürünün ortalama puanını güncelle
        Product product = savedReview.getProduct();
        
        //product.updateAvgRating();
        productRepository.save(product);

        return savedReview;
    }

    public Review updateReview(Long id, Review updatedReview) {
        Review review = getReviewById(id);
        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());
        review.setProduct(updatedReview.getProduct());
        review.setCustomer(updatedReview.getCustomer());

        Review saved = reviewRepository.save(review);

        // Ürün puanını tekrar güncelle
        Product product = review.getProduct();
        productService.updateAvgRating(product);
        productRepository.save(product);

        return saved;
    }

    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        Product product = review.getProduct();
        
        reviewRepository.deleteById(id);

        // Silindikten sonra ürün ortalama ratingi güncellenmeli
        productService.updateAvgRating(product);
        productRepository.save(product);
    }

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public List<Review> getReviewsByCustomer(Customer customer) {
        return reviewRepository.findByCustomer(customer);
    }
}
