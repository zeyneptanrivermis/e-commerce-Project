package com.example.ecommerce_api.controller.Product;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.services.Product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//bitmedi
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;

    @Autowired // Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 🔵 Tüm ürünleri listele
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
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
}
