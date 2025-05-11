/*package com.example.ecommerce_api.loader;

import com.example.ecommerce_api.entity.UserEntity.*;
import com.example.ecommerce_api.repository.UserRepositories.AddressRepository;
import com.example.ecommerce_api.repository.UserRepositories.RoleRepository;
import com.example.ecommerce_api.repository.UserRepositories.SellerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class SellerDataLoader implements CommandLineRunner {

    private final SellerRepository sellerRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public SellerDataLoader(SellerRepository sellerRepository, RoleRepository roleRepository,
                            AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.sellerRepository = sellerRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // ROLE_SELLER hazır mı? Değilse ekle
        Role sellerRole = roleRepository.findByName(UserRole.ROLE_SELLER.name())
        .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_SELLER.name())));



        for (int i = 1; i <= 100; i++) {
            String email = "seller" + i + "@example.com";

            if (sellerRepository.findByEmail(email).isPresent()) continue; // Zaten varsa geç

            Seller seller = new Seller();
            seller.setEmail(email);
            seller.setPassword(passwordEncoder.encode("123456"));
            seller.setName("Seller" + i);
            seller.setSurname("Test");
            seller.setGender(i % 2 == 0 ? Gender.MALE : Gender.FEMALE);
            seller.setDateOfBirth(LocalDate.of(1990 + (i % 10), 1 + (i % 12), 1 + (i % 28)));
            seller.setRoles(Set.of(sellerRole));

            // Adres oluştur ve ilişkilendir
            Address address = new Address();
            address.setCity("İstanbul");
            address.setDistrict("Kadıköy");
            address.setUser(seller); // Bu setter, seller.getAddressList() içine de ekliyor
            seller.setStockAddress(address);

            // Kaydet
            sellerRepository.save(seller);
        }

        System.out.println("✅ 100 seller başarıyla eklendi.");
    }
}
*/
