package com.example.ecommerce_api.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;


    public void addProductToWishlist(Customer customer, Long productId) {
        try {
            System.out.println("Wishlist'e eklenecek ürün ID: " + productId);
    
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    
            customer.addToWishlist(product);
            customerRepository.save(customer);
    
            System.out.println("Ürün wishlist'e eklendi");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Wishlist'e eklenirken hata: " + e.getMessage());
        }
    }
    
    
    public void removeProductFromWishlist(Customer customer, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    
        customer.removeFromWishlist(product);
        customerRepository.save(customer);
    }
    
}
