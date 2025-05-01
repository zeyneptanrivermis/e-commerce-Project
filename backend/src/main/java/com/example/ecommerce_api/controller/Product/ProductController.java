package com.example.ecommerce_api.controller.Product;

import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.dto.ReviewDTO;
import com.example.ecommerce_api.dto.SellerDTO;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.services.Product.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//bitmedi
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;
    private ProductRepository productRepository;

    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository=productRepository;
    }

    // 🔵 Tüm ürünleri listele
@GetMapping
public List<ProductDTO> getAllProducts() {
    return productService.getAllProducts().stream()
        .map(product -> new ProductDTO(
            product.getProductId(),
            product.getProductName(),
            product.getPrice(),
            new SellerDTO( // ← doğru şekilde dönüştürüyoruz
                product.getSeller().getUserId(),
                product.getSeller().getName(),
                product.getSeller().getEmail()
            ),
            product.getDescription(),
            product.getAvgRating(),
            product.getShippingCost(),
            product.getCategory(),
            product.getStockCount(),
            product.getReviews()
            .stream()
            .map(ReviewDTO::new)  // 💥 asıl düzeltme burada
            .toList()
        ))
        .toList();
}


    // 🔵 ID ile ürün getir
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(new ProductDTO(product));
    }


    // 🔵 Yeni ürün oluştur
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // 🔵 ID ile ürünü güncelle
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    // 🔵 ID ile ürünü sil
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // 🔵 Belirli bir satıcının ürünlerini listele (Opsiyonel ama güzel bir özellik)
    @GetMapping("/seller/{sellerId}")
    public List<Product> getProductsBySeller(@PathVariable Long sellerId) {
        return productService.getProductsBySellerId(sellerId);
    }

    // 🔵 Tüm ürünleri sayfa sayfa getir (scroll için)
    @GetMapping("/paged")
    public List<ProductDTO> getProductsPaged(
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "0") int skip) {
        
        return productService.getProductsPaged(limit, skip).stream()
            .map(product -> new ProductDTO(
                product.getProductId(),
                product.getProductName(),
                product.getPrice(),
                new SellerDTO( // ← doğru şekilde dönüştürüyoruz
                    product.getSeller().getUserId(),
                    product.getSeller().getName(),
                    product.getSeller().getEmail()
                ),
                product.getDescription(),
                product.getAvgRating(),
                product.getShippingCost(),
                product.getCategory(),
                product.getStockCount(),
                product.getReviews()
                .stream()
                .map(ReviewDTO::new)  // 💥 asıl düzeltme burada
                .toList()
            ))
            .toList();
    }


}
