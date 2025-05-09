package com.example.ecommerce_api.services.User;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.dto.UserDTO.LoginRequest;
import com.example.ecommerce_api.dto.UserDTO.SellerDTO;
import com.example.ecommerce_api.dto.UserDTO.SellerRegisterDTO;
import com.example.ecommerce_api.entity.UserEntity.Role;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.RoleRepository;
import com.example.ecommerce_api.repository.UserRepositories.SellerRepository;
import com.example.ecommerce_api.services.Auth.AuthService;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    public void registerSeller(SellerRegisterDTO dto) {
        if (sellerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email zaten kayıtlı.");
        }

        Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setSurname(dto.getSurname());
        seller.setShopName(dto.getShopName());
        seller.setEmail(dto.getEmail());
        seller.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role sellerRole = roleRepository.findByName("ROLE_SELLER")
            .orElseThrow(() -> new RuntimeException("Satıcı rolü bulunamadı"));

        seller.setRoles(Set.of(sellerRole));
        sellerRepository.save(seller);
    }

    public String login(LoginRequest request) {
        return authService.login(request);
    }
}

