package com.example.ecommerce_api.services.Cart;

import com.example.ecommerce_api.entity.CartEntity.Cart;
import com.example.ecommerce_api.entity.CartEntity.CartItem;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.CartRepository.CartItemRepository;
import com.example.ecommerce_api.repository.CartRepository.CartRepository;
import com.example.ecommerce_api.repository.ProductRepository.ProductRepository;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;

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
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        List<CartItem> existingItems = cartItemRepository.findByCart(cart);
        for (CartItem item : existingItems) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                cartItemRepository.save(item);
                return;
            }
        }

        CartItem cartItem = new CartItem(product, quantity);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeFromCart(Long customerId, Long productId) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        cartItemRepository.deleteByCartAndProduct(cart, product);
    }

    @Override
    public List<CartItem> listCartItems(Long customerId) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!(user instanceof Customer customer)) {
            throw new RuntimeException("User is not a customer");
        }

        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartItemRepository.findByCart(cart);
    }

    @Override
    public double getCartTotal(Long customerId) {
        List<CartItem> items = listCartItems(customerId);
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
