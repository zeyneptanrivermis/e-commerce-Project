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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    // DTO'lar
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

    public static class RefreshTokenRequest {
        public String refreshToken;
    }

    public static class AuthResponse {
        public String accessToken;
        public String refreshToken;

        public AuthResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    // Login işlemi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email veya şifre hatalı");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email veya şifre hatalı");
        }

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("token", accessToken);
        tokens.put("refreshToken", refreshToken);

        return ResponseEntity.ok(tokens);

    }

    // Kayıt işlemi
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            return ResponseEntity.badRequest().body("Bu email zaten kullanılıyor");
        }

        User user = new Customer();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setName(request.name);
        user.setSurname(request.surname);

        Role role = roleRepository.findByName(UserRole.ROLE_CUSTOMER.name())
        .orElseGet(() -> {
        Role newRole = new Role();
        newRole.setName(UserRole.ROLE_CUSTOMER.name());
        return roleRepository.save(newRole);
    });


        user.setRoles(Collections.singleton(role));

        userRepository.save(user);

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    // Refresh token ile yeni access token üret
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.refreshToken;

        String email = jwtUtil.extractUsername(refreshToken);

        if (email == null || !jwtUtil.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz veya süresi dolmuş refresh token");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        String newAccessToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
    }
}
