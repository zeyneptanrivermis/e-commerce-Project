package com.example.ecommerce_api.services.Order;

import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderItem;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.entity.OrderEntity.Shipping;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.CartRepository.CartItemRepository;
import com.example.ecommerce_api.repository.CartRepository.CartRepository;
import com.example.ecommerce_api.repository.OrderRepository.OrderItemRepository;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.example.ecommerce_api.repository.OrderRepository.ShippingRepository;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Müşteri için yeni sipariş oluştur
    public Order createOrder(Long customerId) {
        Customer customer = (Customer) userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot create order.");
        }

        Order order = new Order();
        order.setCustomer(customer);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(cartItem.getProduct().getPrice());
            return item;
        }).collect(Collectors.toList());

        order.setItemList(orderItems);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // Sepeti temizle
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    // Müşterinin siparişlerini listele
    public List<Order> getOrdersByCustomer(Long customerId) {
        Customer customer = (Customer) userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return orderRepository.findByCustomer(customer);
    }

    // Siparişe ödeme kaydet
    public Payment addPaymentToOrder(Long orderId, double amount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setCustomer(order.getCustomer());
        payment.setAmount(amount);
        payment.setStatus("PAID");
        payment.setPaymentDate(java.time.LocalDate.now());

        return paymentRepository.save(payment);
    }

    // Siparişe shipping kaydet
    public Shipping addShippingToOrder(Long orderId, Shipping shippingInfo) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        shippingInfo.setOrder(order);
        return shippingRepository.save(shippingInfo);
    }
}
