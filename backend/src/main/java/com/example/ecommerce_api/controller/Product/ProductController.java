package com.example.ecommerce_api.controller.Product;

import com.example.ecommerce_api.dto.ProductDTO.ProductDTO;
import com.example.ecommerce_api.dto.ProductDTO.ReviewDTO;
import com.example.ecommerce_api.dto.UserDTO.SellerDTO;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.SideCategoryService;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.services.Product.ProductService;
import com.example.ecommerce_api.entity.ProductEntity.Category;
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
                .map(ReviewDTO::new) 
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

    @GetMapping("/popular") // 🔧 düzeltildi
    public List<Product> getPopularProducts(@RequestParam(defaultValue = "10") int count) {
        return productService.getRandomProducts(count);
    }

    @GetMapping("/filter")
    public List<ProductDTO> getProductsByCategory(@RequestParam String category) {
        List<Product> products;

        String normalizedParam = normalize(category); // normalize yap

        try {
            // Ana kategori (enum) olarak kontrol et
            Category enumCategory = Category.valueOf(normalizedParam);
            products = productService.getProductsByCategory(enumCategory);
        } catch (IllegalArgumentException e) {
            // Değilse side category olarak ara
            products = productService.getAllProducts().stream()
                .filter(p -> p.getSideCategories() != null &&
                            p.getSideCategories().stream()
                                .map(this::normalize)
                                .anyMatch(sc -> sc.equalsIgnoreCase(normalizedParam)))
                .toList();
        }

        return products.stream().map(ProductDTO::new).toList();
    }
    // 🔧 Normalize metodu (boşlukları "_" yap, & → AND, büyük harfe çevir)
    private String normalize(String input) {
        return input.trim().toUpperCase().replaceAll("\\s+", "_").replace("&", "AND");
    }
}
