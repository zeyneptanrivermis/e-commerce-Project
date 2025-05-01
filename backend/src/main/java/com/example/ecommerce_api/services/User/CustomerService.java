package com.example.ecommerce_api.services.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addToWishlist(Long productId, Customer customer) {
        Product product = productRepository.findById(productId).orElseThrow();
        customer.addToWishlist(product);
        customerRepository.save(customer);
    }
    
}
