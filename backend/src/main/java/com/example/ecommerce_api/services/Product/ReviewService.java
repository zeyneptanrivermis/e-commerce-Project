package com.example.ecommerce_api.services.Product;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommerce_api.dto.ProductDTO.ReviewNotAllowedException;
import com.example.ecommerce_api.dto.ProductDTO.ReviewRequestDTO;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.ProductRepository.ReviewRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository   reviewRepository;
    private final ProductRepository  productRepository;
    private final ProductService     productService;
    private final OrderRepository    orderRepository;
    private final CustomerRepository customerRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         ProductRepository productRepository,
                         ProductService productService,
                         OrderRepository orderRepository,
                         CustomerRepository customerRepository) {
        this.reviewRepository   = reviewRepository;
        this.productRepository  = productRepository;
        this.productService     = productService;
        this.orderRepository    = orderRepository;
        this.customerRepository = customerRepository;
    }

    /** Tüm yorumları getirir */
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /** ID ile bir yorumu getirir */
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + id));
    }

    /** Belirli bir müşteri, belirli ürünü teslim aldıysa true döner */
public boolean canReview(Long customerId, Long productId) {
    boolean exists = orderRepository
        .existsByCustomer_UserIdAndItemList_Product_ProductIdAndStatus(
            customerId, productId, OrderStatus.COMPLETED);
    System.out.println("canReview? user=" + customerId + " product=" + productId + " => " + exists);
    return exists;
}

    /** Yeni yorum ekler (sadece COMPLETED siparişler için izin verilir) */
    public Review createReview(Long customerId, ReviewRequestDTO dto) {
        if (!canReview(customerId, dto.getProductId())) {
            throw new ReviewNotAllowedException();
        }

        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + dto.getProductId()));

        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        Review saved = reviewRepository.save(review);

        // Ürünün ortalama puanını güncelle
        productService.updateAvgRating(product);
        productRepository.save(product);

        return saved;
    }

    /** Yorum günceller (sadece yorumu yazan müşteri izinli) */
    public Review updateReview(Long reviewId, Long customerId, ReviewRequestDTO dto) {
        Review existing = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("Review not found: " + reviewId));
        if (!existing.getCustomer().getUserId().equals(customerId)) {
            throw new AccessDeniedException("Bu yorumu düzenleme yetkiniz yok.");
        }

        existing.setRating(dto.getRating());
        existing.setComment(dto.getComment());
        existing.setEdited(true);
        Review saved = reviewRepository.save(existing);

        // Ortalama puanı yeniden hesapla
        productService.updateAvgRating(existing.getProduct());
        productRepository.save(existing.getProduct());

        return saved;
    }

    /** Yorum siler (sadece yorumu yazan müşteri izinli) */
    public void deleteReview(Long reviewId, Long customerId) {
        Review existing = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("Review not found: " + reviewId));
        if (!existing.getCustomer().getUserId().equals(customerId)) {
            throw new AccessDeniedException("Bu yorumu silme yetkiniz yok.");
        }

        Product product = existing.getProduct();
        reviewRepository.delete(existing);

        // Ortalama puanı yeniden hesapla
        productService.updateAvgRating(product);
        productRepository.save(product);
    }

    /** Ürüne ait yorumları getirir */
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    /** Müşteriye ait yorumları getirir */
    public List<Review> getReviewsByCustomer(Customer customer) {
        return reviewRepository.findByCustomer(customer);
    }
}
