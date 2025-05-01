package com.example.ecommerce_api.services.Cart;

import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.CartRepository.CartItemRepository;
import com.example.ecommerce_api.repository.CartRepository.CartRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addToCart(Long customerId, Long productId, int quantity) {
        Customer customer = getCustomerById(customerId);
        Product product = getProductById(productId);

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        cartItemRepository.findByCart(cart).stream()
            .filter(item -> item.getProduct().getProductId().equals(productId))
            .findFirst()
            .ifPresentOrElse(existingItem -> {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                cartItemRepository.save(existingItem);
            }, () -> {
                CartItem cartItem = new CartItem(product, quantity);
                cartItem.setCart(cart);
                cartItemRepository.save(cartItem);
            });
    }

    @Override
    public void removeFromCart(Long customerId, Long productId) {
        Customer customer = getCustomerById(customerId);
        Product product = getProductById(productId);

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCartAndProduct(cart, product);
    }

    @Override
    public List<CartItem> listCartItems(Long customerId) {
        Customer customer = getCustomerById(customerId);

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartItemRepository.findByCart(cart);
    }

    @Override
    public double getCartTotal(Long customerId) {
        return listCartItems(customerId).stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    // -------------------- YARDIMCI METODLAR --------------------

    private Customer getCustomerById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        return customer;
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void updateQuantity(Long customerId, Long productId, int quantity) {
        Customer customer = getCustomerById(customerId);
        Product product = getProductById(productId);
    
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));
    
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }
}
