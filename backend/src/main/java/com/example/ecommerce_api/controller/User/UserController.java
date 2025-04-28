package com.example.ecommerce_api.controller.User;

import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepository.UserRepository;
import com.example.ecommerce_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Eksik Authorization header");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token geçersiz");
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Kullanıcı bulunamadı");
        }

        // Güvenli şekilde kullanıcı bilgisi dönüyoruz
        return ResponseEntity.ok(user);
    }



    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User updatedUser) {
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
        return ResponseEntity.badRequest().body("Eksik Authorization header");
    }

    String token = authorizationHeader.substring(7);
    String email = jwtUtil.extractUsername(token);

    if (email == null) {
        return ResponseEntity.badRequest().body("Token geçersiz");
    }

    User existingUser = userRepository.findByEmail(email)
            .orElse(null);

    if (existingUser == null) {
        return ResponseEntity.badRequest().body("Kullanıcı bulunamadı");
    }

    // Sadece güncellenebilir alanları değiştirelim
    existingUser.setName(updatedUser.getName());
    existingUser.setSurname(updatedUser.getSurname());
    existingUser.setEmail(updatedUser.getEmail());
    existingUser.setDateOfBirth(updatedUser.getDateOfBirth());

    userRepository.save(existingUser);

    return ResponseEntity.ok("Kullanıcı başarıyla güncellendi");
}

}
