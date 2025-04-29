/*package com.example.ecommerce_api.loader;

import com.example.ecommerce_api.entity.ProductEntity.Category;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Seller;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepository.SellerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class ProductDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductDataLoader(ProductRepository productRepository, SellerRepository sellerRepository) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public void run(String... args) {

        List<Seller> sellers = sellerRepository.findAll();

        if (sellers.isEmpty()) {
            System.out.println("\u26a0\ufe0f Seller listesi boş, önce seller oluşturulmalı!");
            return;
        }

        Random random = new Random();
        int imageCounter = 1;

        for (Seller seller : sellers) {
            for (int i = 1; i <= 10; i++) {
                Product product = new Product();

                product.setProductName("Product " + i + " of Seller " + seller.getUserId());
                product.setPrice(50 + random.nextInt(500)); // 50-550 TL arası fiyat
                product.setDescription("Awesome product number " + i + " for seller " + seller.getName());
                product.setCategory(randomCategory());
                product.setShippingCost(10 + random.nextInt(40)); // 10-50 TL shipping
                product.setStockCount(10 + random.nextInt(90)); // 10-100 adet stok
                product.setAvgRating(3.0 + (2.0 * random.nextDouble())); // 3.0 - 5.0 rating

                // Image URL ekle
                product.setImageUrl("https://picsum.photos/200/300?random=" + imageCounter);
                imageCounter++;

                // Seller ile ilişkilendir
                product.setSeller(seller);

                // Ürünü kaydet
                productRepository.save(product);
            }
        }

        System.out.println("\u2705 Tum sellerlara toplam " + (sellers.size() * 10) + " adet product eklendi.");
    }

    private Category randomCategory() {
        Category[] categories = Category.values();
        Random rand = new Random();
        return categories[rand.nextInt(categories.length)];
    }
}

*/