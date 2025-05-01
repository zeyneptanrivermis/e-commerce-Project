package com.example.ecommerce_api.services.User;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.UserEntity.Customer;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepository.UserRepository;
import com.example.ecommerce_api.security.CustomerDetails;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = (Customer) userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new CustomerDetails(customer);
    }

}
