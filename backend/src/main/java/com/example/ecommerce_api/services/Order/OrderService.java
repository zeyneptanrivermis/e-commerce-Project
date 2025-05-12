package com.example.ecommerce_api.services.Order;

import com.example.ecommerce_api.dto.OrderDTO.OrderDTO;
import com.example.ecommerce_api.dto.OrderDTO.OrderItemDTO;
import com.example.ecommerce_api.dto.OrderDTO.PaymentCompleteRequest;
import com.example.ecommerce_api.dto.OrderDTO.PaymentDTO;
import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderItem;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.OrderEntity.Payment;
import com.example.ecommerce_api.entity.OrderEntity.Shipping;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.repository.CartRepository.CartItemRepository;
import com.example.ecommerce_api.repository.CartRepository.CartRepository;
import com.example.ecommerce_api.repository.OrderRepository.OrderItemRepository;
import com.example.ecommerce_api.repository.OrderRepository.OrderRepository;
import com.example.ecommerce_api.repository.OrderRepository.PaymentRepository;
import com.example.ecommerce_api.repository.OrderRepository.ShippingRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.CustomerRepository;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeListParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.security.core.Authentication;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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

    @Autowired
    private PaymentService stripePaymentService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    private final ModelMapper mapper;

        public OrderService(OrderRepository orderRepo,
                        CustomerRepository customerRepo,
                        ModelMapper mapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }


    /**
     * Verilen orderId için Stripe PaymentIntent yaratır,
     * intent ID'yi ve clientSecret'i kaydeder, clientSecret'i döner.
     */
    public String createStripePayment(Long orderId, String currency) throws StripeException {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    long amount = Math.round(order.getOrderTotalWithoutDiscount() * 100);

    // --- burası değişti ---
    PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
        .setAmount(amount)
        .setCurrency(currency)
        .putMetadata("orderId", orderId.toString())
        .build();
    PaymentIntent intent = PaymentIntent.create(params);
    // --- değişiklik bitti ---

    Payment payment = new Payment();
    payment.setOrder(order);
    payment.setCustomer((Customer) order.getCustomer());
    payment.setAmount(amount);
    payment.setStripePaymentIntentId(intent.getId());
    payment.setStatus(intent.getStatus());
    payment.setPaymentDate(null);
    paymentRepository.save(payment);

    return intent.getClientSecret();
    }

     /**
     * Frontend'den gelen sipariş ve ürün listesini işler, müşteri bilgisi controller'dan set edilir.
     */
    public Order createOrderWithItems(Order incomingOrder) {
        if (incomingOrder.getCustomer() == null) {
            throw new RuntimeException("Customer information is missing.");
        }

        // Sipariş içindeki ürünlerin fiyatlarını güncelle ve ilişkilendir
        for (OrderItem item : incomingOrder.getItemList()) {
            item.setOrder(incomingOrder);
            item.setPrice(item.getProduct().getPrice()); // üründen güncel fiyatı al
        }

        return orderRepository.save(incomingOrder); // cascade = ALL sayesinde OrderItem'lar da kaydedilir
    }

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        
        return orders.stream().map(order -> {
            // Ödeme varsa al, yoksa null bırak
            Payment payment = paymentRepository.findByOrder(order).orElse(null);

            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getOrderId());
            dto.setTotalWithDiscount(order.getOrderTotalWithDiscount());
            dto.setTotalWithoutDiscount(order.getOrderTotalWithoutDiscount());

            if (payment != null) {
                dto.setStatus(payment.getStatus());
                dto.setPaymentDate(payment.getPaymentDate());
            } else {
                dto.setStatus("UNPAID");  // ← Frontend bu değeri yakalayıp DENIED gibi gösterebilir
                dto.setPaymentDate(null);
            }
            List<OrderItemDTO> itemDtos = order.getItemList().stream()
                    .map(OrderItemDTO::fromEntity)
                    .toList();

            dto.setItemList(itemDtos);

            return dto;
        }).toList();
    }

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

    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findAllById(Collections.singletonList(userId));

        return orders.stream().map(order -> {
            Payment payment = paymentRepository.findByOrder(order)
                    .orElseThrow(() -> new RuntimeException("Payment not found for the order."));
            
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getOrderId());
            dto.setTotalWithDiscount(order.getOrderTotalWithDiscount());
            dto.setTotalWithoutDiscount(order.getOrderTotalWithoutDiscount());
            dto.setStatus(payment.getStatus());
            dto.setPaymentDate(payment.getPaymentDate());

            List<OrderItemDTO> itemDtos = order.getItemList().stream()
                    .map(OrderItemDTO::fromEntity) 
                    .toList();

            dto.setItemList(itemDtos);

            return dto;
        }).toList();
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

    @Transactional
public void finalizePayment(Long orderId, PaymentCompleteRequest req) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order bulunamadı"));

    Payment payment = paymentRepository.findByStripePaymentIntentId(req.getPaymentIntentId())
        .orElseThrow(() -> new RuntimeException("Payment bulunamadı"));

    payment.setStatus("succeeded");
    payment.setPaymentDate(LocalDate.now());

    // 🔥 Charge ID sadece webhook'tan gelecek, frontend'ten değil
    if (req.getChargeId() != null) {
        payment.setStripeChargeId(req.getChargeId());
    }

    paymentRepository.save(payment);

    order.setStatus(OrderStatus.COMPLETED);
    order.setPaymentDate(LocalDateTime.now());
    order.setPaymentIntentId(req.getPaymentIntentId());
    order.setPaidAmount(req.getAmount()); // varsa
    orderRepository.save(order);
}



    public OrderDTO createOrderDTO(Long customerId) {
        Order order = createOrder(customerId); // mevcut fonksiyonunu kullan
        // Order'ı OrderDTO'ya map et
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setTotalWithDiscount(order.getOrderTotalWithDiscount());
        dto.setTotalWithoutDiscount(order.getOrderTotalWithoutDiscount());
        // ... diğer alanları da ekle ...
        // itemList ve paymentInfo'yu da uygun şekilde ekle
        return dto;
    }

    @Transactional
    public List<OrderDTO> getOrderHistoryForUser(Authentication auth) {
    Customer customer = customerRepository.findByEmail(auth.getName())
        .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı"));

    List<Order> orders = orderRepository.findByCustomerOrderByPaymentDateAsc(customer);

    return orders.stream()
        .map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getOrderId());
            if (order.getPaymentDate() != null) {
                dto.setPaymentDate(order.getPaymentDate().toLocalDate());
            }
            dto.setStatus(order.getStatus().name());
            dto.setTotalWithoutDiscount(order.getOrderTotalWithoutDiscount());
            dto.setTotalWithDiscount(order.getOrderTotalWithDiscount());
            // itemList, paymentInfo vs.
            return dto;
        })
        .collect(Collectors.toList());
    }

    @Transactional
    public void markOrderAsPaid(Long orderId, PaymentCompleteRequest req) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order bulunamadı"));
    order.setStatus(OrderStatus.COMPLETED);
    order.setPaymentDate(LocalDateTime.now());
    order.setPaymentIntentId(req.getPaymentIntentId());
    order.setPaidAmount(req.getAmount());
    orderRepository.save(order);
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        Payment payment = paymentRepository.findByOrder(order)
            .orElse(null);
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setTotalWithDiscount(order.getOrderTotalWithDiscount());
        dto.setTotalWithoutDiscount(order.getOrderTotalWithoutDiscount());
        if (payment != null) {
            dto.setStatus(payment.getStatus());
            dto.setPaymentDate(payment.getPaymentDate());
        } else {
            dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
            dto.setPaymentDate(order.getPaymentDate() != null ? order.getPaymentDate().toLocalDate() : null);
        }
        if (order.getItemList() != null) {
            List<OrderItemDTO> itemDtos = order.getItemList().stream()
                .map(OrderItemDTO::fromEntity)
                .toList();
            dto.setItemList(itemDtos);
        }
        return dto;
    }
    public void updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != newStatus) {
            order.setStatus(newStatus);
            orderRepository.save(order);
        }
    }

    public void refundOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı."));

        Payment payment = paymentRepository.findByOrder(order)
            .orElseThrow(() -> new RuntimeException("Ödeme bilgisi bulunamadı."));

        String chargeId = payment.getStripeChargeId();
        if (chargeId == null || chargeId.isBlank()) {
            throw new RuntimeException("Charge ID bulunamadı. finalizePayment() eksik olabilir.");
        }

        try {
            stripePaymentService.refundCharge(chargeId);  // ✅ Burada kullanılıyor
        } catch (StripeException e) {
            throw new RuntimeException("Stripe refund hatası: " + e.getMessage());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setPaymentDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void cancelOrderByAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı."));

        if (order.getStatus() == OrderStatus.SHIPPED ||
            order.getStatus() == OrderStatus.COMPLETED ||
            order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Bu sipariş iptal edilemez.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    public void requestRefund(Long orderId) {
            Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));
            if (order.getStatus() != OrderStatus.COMPLETED && order.getStatus() != OrderStatus.SHIPPED) {
                throw new IllegalStateException("Sadece tamamlanmış veya kargolanmış siparişler için iade talebi oluşturulabilir.");
            }
            order.setStatus(OrderStatus.REFUND_REQUESTED);
            orderRepository.save(order);
        }

    public List<OrderDTO> getOrdersBySellerId(Long sellerId) {
        List<Order> orders = orderRepository.findOrdersBySellerId(sellerId);

        return orders.stream()
            .map(order -> {
                OrderDTO dto = new OrderDTO();
                dto.setOrderId(order.getOrderId());
                dto.setTotalWithDiscount(order.getOrderTotalWithDiscount());
                dto.setTotalWithoutDiscount(order.getOrderTotalWithoutDiscount());
                dto.setStatus(order.getStatus().name());

                // Map itemList
                List<OrderItemDTO> itemDtos = order.getItemList().stream()
                    .filter(item -> item.getProduct().getSeller().getUserId().equals(sellerId))
                    .map(OrderItemDTO::fromEntity)
                    .toList();
                dto.setItemList(itemDtos);

                // Map payment info if exists
                Payment payment = paymentRepository.findByOrder(order).orElse(null);
                if (payment != null) {
                    dto.setPaymentInfo(PaymentDTO.fromEntity(payment));
                    dto.setStatus(payment.getStatus());
                    dto.setPaymentDate(payment.getPaymentDate());
                } else {
                    dto.setPaymentInfo(null);
                    dto.setPaymentDate(null);
                }

                return dto;
            })
            .collect(Collectors.toList());
    }

    public void requestRefund(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Sipariş bulunamadı: " + orderId));
        if (order.getStatus() != OrderStatus.COMPLETED && order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Sadece tamamlanmış veya kargolanmış siparişler için iade talebi oluşturulabilir.");
        }
        order.setStatus(OrderStatus.REFUND_REQUESTED);
        orderRepository.save(order);
    }

}