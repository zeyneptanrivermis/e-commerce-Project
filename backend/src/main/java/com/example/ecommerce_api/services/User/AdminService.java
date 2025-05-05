package com.example.ecommerce_api.services.User;

import com.example.ecommerce_api.dto.UserDTO.AdminUserDTO;
import com.example.ecommerce_api.dto.UserDTO.StatsDTO;
import com.example.ecommerce_api.dto.UserDTO.UserDTO;
import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.repository.UserRepositories.AdminRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;
import com.example.ecommerce_api.repository.UserRepositories.SellerRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(
            AdminRepository adminRepository,
            CustomerRepository customerRepository,
            SellerRepository sellerRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            PasswordEncoder passwordEncoder) {
        this.adminRepository     = adminRepository;
        this.customerRepository  = customerRepository;
        this.sellerRepository    = sellerRepository;
        this.productRepository   = productRepository;
        this.orderRepository     = orderRepository;
        this.passwordEncoder     = passwordEncoder;
    }

    // --- Admin CRUD ---

    @PreAuthorize("hasRole('ADMIN')")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // --- Statistics ---

    @PreAuthorize("hasRole('ADMIN')")
    public StatsDTO getStats() {
        long userCount     = adminRepository.count();                        // toplam admin
        long productCount  = productRepository.count();                     // toplam ürün
        long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING); // bekleyen sipariş
        return new StatsDTO(userCount, productCount, pendingOrders);
    }

    // --- Customer ban/unban ---

    @PreAuthorize("hasRole('ADMIN')")
    public void toggleCustomerBan(Long id) {
        Customer c = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        c.setBanned(!Boolean.TRUE.equals(c.getBanned()));
        customerRepository.save(c);
    }

    // --- Seller ban/unban ---

    @PreAuthorize("hasRole('ADMIN')")
    public void toggleSellerBan(Long id) {
        Seller s = sellerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Seller not found with id: " + id));
        s.setBanned(!Boolean.TRUE.equals(s.getBanned()));
        sellerRepository.save(s);
    }

    // --- Product cancel (soft-delete) ---

    @PreAuthorize("hasRole('ADMIN')")
    public void cancelProduct(Long id) {
        Product p = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        p.setCancelled(true);
        productRepository.save(p);
    }

     /** Tüm müşterileri getir ve AdminUserDTO'ya çevir */
  @PreAuthorize("hasRole('ADMIN')")
  public List<AdminUserDTO> getAllCustomersDto() {
    return customerRepository.findAll()
      .stream()
      .map(c -> new AdminUserDTO(
          c.getUserId(),
          c.getName(),
          c.getEmail(),
          Collections.singletonList("ROLE_CUSTOMER"),
          c.getBanned()
      ))
      .collect(Collectors.toList());
  }

  /** Tüm satıcıları getir ve AdminUserDTO'ya çevir */
  @PreAuthorize("hasRole('ADMIN')")
  public List<AdminUserDTO> getAllSellersDto() {
    return sellerRepository.findAll()
      .stream()
      .map(s -> new AdminUserDTO(
          s.getUserId(),
          s.getName(),
          s.getEmail(),
          Collections.singletonList("ROLE_SELLER"),
          s.getBanned()
      ))
      .collect(Collectors.toList());
  }
}
