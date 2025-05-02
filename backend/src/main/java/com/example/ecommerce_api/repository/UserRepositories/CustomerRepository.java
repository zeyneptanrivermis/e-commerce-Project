package com.example.ecommerce_api.repository.UserRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ecommerce_api.entity.UserEntity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c JOIN FETCH c.wishlist WHERE c.id = :id")
    Optional<Customer> findByIdWithWishlist(@Param("id") Long id);



    Optional<Customer> findByEmail(String email);
}
