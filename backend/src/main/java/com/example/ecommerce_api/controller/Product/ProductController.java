package com.example.ecommerce_api.controller.Product;

import com.example.ecommerce_api.dto.ProductDTO;
import com.example.ecommerce_api.dto.SellerDTO;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.services.Product.ProductService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
//bitmedi
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
            product.getStockCount()
        ))
        .toList();
}


    // 🔵 ID ile ürün getir
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
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
            new SellerDTO(
                product.getSeller().getUserId(),
                product.getSeller().getName(),
                product.getSeller().getEmail()
            ),
            product.getDescription(),
            product.getAvgRating(),
            product.getShippingCost(),
            product.getCategory(),
            product.getStockCount()
        ))
            .toList();
    }
    


}
