package com.example.ecommerce_api.services.User;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.Role;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;
import com.example.ecommerce_api.repository.UserRepositories.RoleRepository;

@Service
public class CustomerService {

    @Autowired
    private RoleRepository roleRepository;

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

    public void updateCustomer(Long id, Customer updated) {
    Customer existing = customerRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

    existing.setName(updated.getName());
    existing.setEmail(updated.getEmail());

    // Roller doğrudan setlenirse hata alırsın, önce DB'den bulmamız gerekiyor
    Set<Role> roles = updated.getRoles().stream()
        .map(r -> roleRepository.findByName(r.getName())
            .orElseThrow(() -> new RuntimeException("Role not found: " + r.getName())))
        .collect(Collectors.toSet());

    existing.setRoles(roles);

    customerRepository.save(existing);
}
    
    
    public void removeProductFromWishlist(Customer customer, Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    
        customer.removeFromWishlist(product);
        customerRepository.save(customer);
    }
    
}
