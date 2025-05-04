package com.example.ecommerce_api.services.Recommendation;

import com.example.ecommerce_api.dto.RecommendationDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RecommendationService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public RecommendationService(ProductRepository productRepository,
                                 OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Kullanıcıya önerilecek ürünleri getirir.
     * @param userId Öneri yapılacak kullanıcı ID'si
     * @param limit Döndürülecek öneri sayısı
     * @return Profile temelli Jaccard skoruna göre sıralı öneriler
     */
    public List<RecommendationDTO> recommendProducts(Long userId, int limit) {
        List<Product> purchased = orderRepository.findProductsByUserId(userId);

        Set<String> profileTags = purchased.stream()
                .flatMap(p -> {
                    Set<String> tags = new HashSet<>();
                    if (p.getCategory() != null) {
                        tags.add(p.getCategory().name());
                    }
                    if (p.getSideCategories() != null) {
                        tags.addAll(p.getSideCategories());
                    }
                    return tags.stream();
                })
                .collect(Collectors.toSet());

                return productRepository.findAll().stream()
                .filter(p -> !purchased.contains(p))
                .<RecommendationDTO>map((Product p) -> new RecommendationDTO(p.getProductId(), computeJaccard(profileTags, p)))
                .sorted(Comparator.comparingDouble(RecommendationDTO::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double computeJaccard(Set<String> profileTags, Product p) {
        Set<String> productTags = new HashSet<>();
        if (p.getCategory() != null) {
            productTags.add(p.getCategory().name());
        }
        if (p.getSideCategories() != null) {
            productTags.addAll(p.getSideCategories());
        }
        Set<String> intersection = new HashSet<>(profileTags);
        intersection.retainAll(productTags);
        Set<String> union = new HashSet<>(profileTags);
        union.addAll(productTags);
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    /**
     * Belirli bir ürüne benzer ürünleri döner.
     * @param productId Kaynak ürün ID
     * @param limit     Kaç adet ürün getirilsin
     */
    public List<RecommendationDTO> recommendSimilarProducts(Long productId, int limit) {
        // 1) Kaynak ürünü al
        Product target = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));

        // 2) Tag kümesini oluştur
        Set<String> targetTags = new HashSet<>();
        if (target.getCategory() != null) {
            targetTags.add(target.getCategory().name());
        }
        if (target.getSideCategories() != null) {
            targetTags.addAll(target.getSideCategories());
        }

        // 3) Tüm ürünlerle Jaccard benzerliği hesapla
        return productRepository.findAll().stream()
            .filter(p -> !p.getProductId().equals(productId))             // kendisi hariç
            .map(p -> {
                double score = computeJaccard(targetTags, p);
                return new RecommendationDTO(p.getProductId(), score);
            })
            .sorted(Comparator.comparingDouble(RecommendationDTO::getScore).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }
}
