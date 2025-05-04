package com.example.ecommerce_api.controller.Recommendation;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.dto.ProductDTO.RecommendationDTO;
import com.example.ecommerce_api.services.Recommendation.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * @param userId Kullanıcı ID
     * @param limit  Döndürülecek öneri sayısı (default: 5)
     * @return Öneri listesi
     */
    @GetMapping
    public List<RecommendationDTO> getRecommendations(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        return recommendationService.recommendProducts(userId, limit);
    }

    @GetMapping("/similar")
    public List<RecommendationDTO> getSimilar(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "5") int limit) {
        return recommendationService.recommendSimilarProducts(productId, limit);
    }
}
