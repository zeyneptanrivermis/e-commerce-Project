package com.example.ecommerce_api.controller.Auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.ecommerce_api.entity.UserEntity.*;
import com.example.ecommerce_api.repository.UserRepository.RoleRepository;
import com.example.ecommerce_api.repository.UserRepository.UserRepository;
import com.example.ecommerce_api.security.JwtUtil;

import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //ic sinif
    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class RegisterRequest {
        public String email;
        public String password;
        public String name;
        public String surname;
    }

    public static class AuthResponse {
        public String token;
        private String refreshToken;

        public AuthResponse(String token, String refreshToken) {
            this.token = token;
            this.refreshToken=refreshToken;
        }
        public AuthResponse(String token){
            this.token=token;
        }
    }

    public static class RefreshTokenRequest {
        public String refreshToken;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(401).body("Email veya şifre hatalı");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            return ResponseEntity.status(401).body("Email veya şifre hatalı");
        }

        String token = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return ResponseEntity.ok(new AuthResponse(token, refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            return ResponseEntity.badRequest().body("Bu email zaten kullanılıyor");
        }
    
        User user = new Customer(); // Register sadece Customer olacak
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setName(request.name);
        user.setSurname(request.surname);
    
        // Varsayılan rolü kontrol et: ROLE_CUSTOMER
        Role role = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    // Eğer yoksa yeni bir Role oluştur
                    Role newRole = new Role();
                    newRole.setName("ROLE_CUSTOMER");
                    return roleRepository.save(newRole); // Veritabanına kaydet
                });
    
        user.setRoles(Collections.singleton(role)); // Kullanıcıya rolü ata
    
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }



}
