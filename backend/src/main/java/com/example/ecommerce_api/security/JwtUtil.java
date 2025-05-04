package com.example.ecommerce_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.example.ecommerce_api.entity.UserEntity.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // 256-bit secret key (minimum 32 characters)
    private final String SECRET = "superSecretKeyForJwtGeneration1234567890";
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", user.getRoles().stream()
            .map(role -> role.getName())
            .collect(Collectors.toList()));

        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());
        claims.put("surname", user.getSurname());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", user.getRoles().stream()
            .map(role -> role.getName())
            .collect(Collectors.toList()));

        claims.put("userId", user.getUserId());
        claims.put("name", user.getName());
        claims.put("surname", user.getSurname());
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 gün
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            String username = claims.getSubject();  // Username'i al
            return username;
        } catch (JwtException e) {
            System.err.println("Token çözme hatası: " + e.getMessage());
            return null;
        }
    }

    public String extractRole(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public Long extractUserId(String token) {
        // Extract userId from the token
        return (Long) Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
    }

    public String extractNameFromToken(String token) {
        // Extract the user's name (not username)
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("name");
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
    
    public boolean isRefreshTokenValid(String token) {
        try {
            Date expiration = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
