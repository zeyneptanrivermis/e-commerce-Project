package com.example.ecommerce_api.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.OrderEntity.Order;
import com.example.ecommerce_api.entity.OrderEntity.OrderStatus;
import com.example.ecommerce_api.entity.ProductEntity.Product;
import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.Seller;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Belirli bir müşterinin tüm siparişlerini getir
    List<Order> findByCustomer(Customer customer);

    // Eğer customerId üzerinden çağırmak istersen
    List<Order> findByCustomer_UserId(Long customerId);

    @Query("select oi.product from OrderItem oi where oi.order.customer.id = :userId")
    List<Product> findProductsByUserId(@Param("userId") Long userId);

    Optional<Order> findTopByCustomerOrderByOrderIdDesc(Customer customer);

    long countByStatus(OrderStatus  status);

    long countBySeller(Seller seller);

    List<Order> findByCustomerOrderByPaymentDateAsc(Customer customer);

    boolean existsByCustomer_UserIdAndItemList_Product_ProductIdAndStatus(
        Long customerId,
        Long productId,
        OrderStatus status
    );

        @Query("SELECT DISTINCT o FROM Order o JOIN o.itemList i JOIN i.product p WHERE p.seller.userId = :sellerId")
        List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);


}
