package com.example.ecommerce_api.services.Product;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.data.domain.PageRequest;
import com.example.ecommerce_api.entity.ProductEntity.Category;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.SellerRepository;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(ProductRepository productRepository, SellerRepository sel) {
        this.productRepository = productRepository;
        this.sellerRepository=sel;
    }

    public List<Product> getProductsPaged(int limit, int skip) {
        Pageable pageable = PageRequest.of(skip / limit, limit);
        return productRepository.findAll(pageable).getContent();
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
        //existingProduct.updateAvgRating(); // Ortalama ratingi güncelle
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
        return productRepository.findBySellerUserId(sellerId);
    }

    public boolean updateAvgRating(Product product){
        if (product.getReviews() == null || product.getReviews().isEmpty()) {
            return false;
        }
        int sum = 0;
        for (Review review : product.getReviews()) {
            sum += review.getRating();
        }
        double avg = (double) sum / product.getReviews().size();
        product.setAvgRating(avg);
        return true;
    }
    public List<Product> getRandomProducts(int count) {
        List<Product> all = productRepository.findAll();
        Collections.shuffle(all);
        return all.stream().limit(count).collect(Collectors.toList());
    }

    public void cancelProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow();
        product.setCancelled(true);
        productRepository.save(product);
    }
    
    public List<Product> getProductsBySellerEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Satıcı bulunamadı"));
        return productRepository.findAllBySeller(seller);
    }

    public Product saveProductForSeller(Product product, String sellerEmail) {
        Seller seller = sellerRepository.findByEmail(sellerEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Satıcı bulunamadı"));

        product.setSeller(seller);
        return productRepository.save(product);
    }

    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getProductsByAnyCategory(String category) {
        return productRepository.findByMainOrSideCategory(category);
    }
    private String normalize(String input) {
        return input.trim().toUpperCase().replaceAll("\\s+", "_").replace("&", "AND");
    }

}

