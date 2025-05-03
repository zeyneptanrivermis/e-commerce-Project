/*
package com.example.ecommerce_api.loader;

import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.ProductEntity.Review;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.ProductRepository.ReviewRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class ReviewDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public ReviewDataLoader(ProductRepository productRepository,
                            CustomerRepository customerRepository,
                            ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) {
        List<Product> products = productRepository.findAll();
        List<Customer> customers = customerRepository.findAll();

        if (customers.size() < 2) {
            System.out.println("❗ En az 2 müşteri gerekiyor. Lütfen birkaç Customer oluşturun.");
            return;
        }

        Random random = new Random();
        int commentCounter = 1;

        for (Product product : products) {
            for (int i = 0; i < 2; i++) {
                Customer customer = customers.get(random.nextInt(customers.size()));

                Review review = new Review();
                review.setProduct(product);
                review.setCustomer(customer);
                review.setRating(3 + random.nextInt(3)); // 3–5 puan
                review.setComment("Otomatik yorum #" + commentCounter + " - Ürün " + product.getProductName());
                review.setEdited(random.nextBoolean());

                reviewRepository.save(review);
                commentCounter++;
            }
        }

        System.out.println("✅ Tüm ürünlere 2'şer adet review başarıyla yüklendi.");
    }
}
*/