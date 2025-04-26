package com.example.ecommerce_api.services.Product;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired // Constructor Injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Tüm ürünleri getir
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ID ile bir ürünü getir
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    // Yeni ürün oluştur
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Ürünü güncelle
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setSideCategories(updatedProduct.getSideCategories());
        existingProduct.setShippingCost(updatedProduct.getShippingCost());
        existingProduct.setStockCount(updatedProduct.getStockCount());
        existingProduct.setSeller(updatedProduct.getSeller());
        existingProduct.setDiscounts(updatedProduct.getDiscounts());
        existingProduct.setReviews(updatedProduct.getReviews());
        existingProduct.updateAvgRating(); // Ortalama ratingi güncelle

        return productRepository.save(existingProduct);
    }

    // Ürünü sil
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    // Ekstra: Belirli bir satıcının ürünlerini getir
    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}

