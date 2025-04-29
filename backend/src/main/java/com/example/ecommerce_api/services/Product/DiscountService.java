package com.example.ecommerce_api.services.Product;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.ProductEntity.Discount;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.repository.ProductRepository.DiscountRepository;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discount not found with ID: " + id));
    }

    public Discount createDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Long id, Discount updatedDiscount) {
        Discount discount = getDiscountById(id);
        discount.setName(updatedDiscount.getName());
        discount.setPercentage(updatedDiscount.getPercentage());
        discount.setDiscountCode(updatedDiscount.getDiscountCode());
        discount.setProduct(updatedDiscount.getProduct());
        return discountRepository.save(discount);
    }

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }

    public List<Discount> getDiscountsByProduct(Product product) {
        return discountRepository.findByProduct(product);
    }
}
