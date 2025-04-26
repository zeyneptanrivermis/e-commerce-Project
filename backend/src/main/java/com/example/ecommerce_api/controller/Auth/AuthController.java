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
@RequestMapping("/auth")
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

        User user = new Customer(); // register sadece customer alır, role sonradan değişir uygulama içinde
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setName(request.name);
        user.setSurname(request.surname);

        // Varsayılan rol ata: ROLE_CUSTOMER
        Role role = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Rol bulunamadı"));
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        String refreshToken= jwtUtil.generateRefreshToken(user);
        return ResponseEntity.ok(new AuthResponse(token,refreshToken));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.refreshToken;

        // Refresh token'ı çözümle ve kullanıcıyı bul
        String email = jwtUtil.extractUsername(refreshToken);

        if (email == null || !jwtUtil.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz veya süresi dolmuş refresh token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        // Yeni access token üret
        String newAccessToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(newAccessToken));
    }



}

