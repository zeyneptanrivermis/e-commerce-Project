package com.example.ecommerce_api.services.Auth;

import com.example.ecommerce_api.dto.UserDTO.LoginRequest;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;
import com.example.ecommerce_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public String login(LoginRequest loginRequest) {
        // Kullanıcıyı email ve şifresi ile authenticate ediyoruz
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Kullanıcı bulundu mu kontrol ediyoruz
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        // Eğer authenticate başarılıysa JWT token üretiyoruz
        return jwtUtil.generateToken(user);
    }
}
