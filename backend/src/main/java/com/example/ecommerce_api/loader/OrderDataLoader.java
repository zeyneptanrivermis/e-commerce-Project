/*package com.example.ecommerce_api.loader;

import com.example.ecommerce_api.entity.OrderEntity.*;
import com.example.ecommerce_api.entity.ProductEntity.*;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.*;
import com.example.ecommerce_api.repository.OrderRepository.OrderItemRepository;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.example.ecommerce_api.repository.ProductRepository.DiscountRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class OrderDataLoader implements CommandLineRunner {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final DiscountRepository discountRepository;

    public OrderDataLoader(OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository,
                           ProductRepository productRepository,
                           CustomerRepository customerRepository,
                           PaymentRepository paymentRepository,
                           DiscountRepository discountRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.discountRepository = discountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Kullanıcıyı al
        Customer customer = customerRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 2. Ürünleri oluştur
        Product product1 = new Product();
        product1.setProductName("Bluetooth Kulaklık");
        product1.setPrice(350.0);
        product1.setStockCount(100);
        product1.setShippingCost(15.0);
        product1.setCategory(Category.ELECTRONICS);
        product1.setDescription("Kablosuz kulaklık");
        product1.setImageUrl("https://via.placeholder.com/150");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setProductName("Laptop Çantası");
        product2.setPrice(180.0);
        product2.setStockCount(200);
        product2.setShippingCost(10.0);
        product2.setCategory(Category.PET_SUPPLIES);
        product2.setDescription("Su geçirmez laptop çantası");
        product2.setImageUrl("https://via.placeholder.com/150");
        productRepository.save(product2);

        // 3. Siparişi oluştur
        Order order = new Order();
        order.setCustomer(customer);
        orderRepository.save(order);

        // 4. OrderItem'ları oluştur
        OrderItem item1 = new OrderItem();
        item1.setOrder(order);
        item1.setProduct(product1);
        item1.setQuantity(2);
        item1.setPrice(product1.getPrice());
        orderItemRepository.save(item1);

        OrderItem item2 = new OrderItem();
        item2.setOrder(order);
        item2.setProduct(product2);
        item2.setQuantity(1);
        item2.setPrice(product2.getPrice());
        orderItemRepository.save(item2);

        // 5. Discount opsiyonel, burada kullanmıyoruz ama istersen şunu da ekleyebilirsin:
        /*
        Discount discount = new Discount();
        discount.setDiscountCode("SPRING20");
        discount.setName("Bahar Kampanyası");
        discount.setPercentage(20);
        discount.setProduct(product1);
        discountRepository.save(discount);

        order.setDiscount(discount);
      

        order.setItemList(List.of(item1, item2));
        orderRepository.save(order);

        // 6. Ödeme bilgisi oluştur
        Payment payment = new Payment();
        payment.setCustomer(customer);
        payment.setOrder(order);
        payment.setAmount(order.getOrderTotalWithDiscount());
        payment.setStatus("COMPLETED");
        payment.setPaymentDate(LocalDate.now());
        paymentRepository.save(payment);

        System.out.println("✅ Order ve ödeme verisi başarıyla yüklendi.");
    }
}
*/