package com.example.ecommerce_api.InitialTestData;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.ecommerce_api.entity.ProductEntity.Category;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Admin;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.Role;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepository.AdminRepository;
import com.example.ecommerce_api.repository.UserRepository.CustomerRepository;
import com.example.ecommerce_api.repository.UserRepository.SellerRepository;
import com.example.ecommerce_api.repository.UserRepository.RoleRepository;
import com.github.javafaker.Faker;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final Faker faker = new Faker();

    public DataLoader(RoleRepository roleRepository, CustomerRepository customerRepository, 
                      SellerRepository sellerRepository, AdminRepository adminRepository, 
                      ProductRepository productRepository) {
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.adminRepository = adminRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (customerRepository.count() == 0) {
            createUsers();
        }
        if (productRepository.count() == 0) {
            createProducts();
        }
    }

    private void createUsers() {
        // Customer creation
        if (customerRepository.count() == 0) {
            Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("ROLE_CUSTOMER not found!"));

            for (int i = 0; i < 5; i++) {
                Customer customer = new Customer();
                customer.setName(faker.name().firstName());
                customer.setSurname(faker.name().lastName());
                customer.setEmail(faker.internet().emailAddress());
                customer.setPassword("password123"); // Encode the password
                customer.setRoles(Set.of(customerRole)); // Assign role
                customerRepository.save(customer);
            }
        }

        // Seller creation
        if (sellerRepository.count() == 0) {
            Role sellerRole = roleRepository.findByName("ROLE_SELLER")
                .orElseThrow(() -> new RuntimeException("ROLE_SELLER not found!"));

            for (int i = 0; i < 5; i++) {
                Seller seller = new Seller();
                seller.setName(faker.company().name());
                seller.setSurname("Store");
                seller.setEmail(faker.internet().emailAddress());
                seller.setPassword("password123");
                seller.setRoles(Set.of(sellerRole)); // Assign role
                sellerRepository.save(seller);
            }
        }

        // Admin creation
        if (adminRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found!"));

            Admin admin = new Admin();
            admin.setName("Admin");
            admin.setSurname("User");
            admin.setEmail("admin@example.com");
            admin.setPassword("adminpass");
            admin.setPermission("ALL");
            admin.setRoles(Set.of(adminRole)); // Assign role
            adminRepository.save(admin);
        }
    }

    private void createProducts() {
        List<Category> categories = Arrays.asList(Category.values());
        List<Seller> sellers = sellerRepository.findAll();

        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setProductName(faker.commerce().productName());
            String rawPrice = faker.commerce().price();
            double price = Double.parseDouble(rawPrice.replace(",", ".")); // Correct the price format
            product.setPrice(price);
            product.setCategory(categories.get(i % categories.size())); // Use enum Category directly
            product.setSeller(sellers.get(i % sellers.size()));
            productRepository.save(product);
        }
    }
}
