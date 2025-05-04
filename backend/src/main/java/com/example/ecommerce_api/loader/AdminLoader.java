/*package com.example.ecommerce_api.loader;

import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.entity.UserEntity.Role;
import com.example.ecommerce_api.entity.UserEntity.UserRole;
import com.example.ecommerce_api.repository.UserRepositories.AdminRepository;
import com.example.ecommerce_api.repository.UserRepositories.RoleRepository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;

@Component
public class AdminLoader {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        String adminEmail = "admin@a.com";

        // Admin zaten varsa, eklemeye gerek yok
        if (adminRepository.findByEmail(adminEmail).isPresent()) {
            System.out.println("⚠ Admin zaten mevcut: " + adminEmail);
            return;
        }

        // Admin rolü var mı kontrol et, yoksa oluştur
        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN.name())
            .orElseGet(() -> {
                Role role = new Role();
                role.setName(UserRole.ROLE_ADMIN.name());
                return roleRepository.save(role);
            });

        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode("12345678wW")); // Şifre hashleniyor
        admin.setName("Admin");
        admin.setSurname("User");
        admin.setGender(null); // isteğe göre ayarlanabilir
        admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
        admin.setRoles(Collections.singleton(adminRole)); // BURASI düzeltildi

        adminRepository.save(admin);

        System.out.println("✔ Admin kullanıcı başarıyla yüklendi: " + adminEmail);
    }
}
*/