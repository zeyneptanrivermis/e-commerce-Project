package com.example.ecommerce_api.services.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.entity.UserEntity.Address;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepositories.AddressRepository;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public Address addAddressToUser(Long userId, Address address) {
        // 1. Kullanıcıyı bul
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + userId));

        // 2. Adresi kullanıcıya bağla
        address.setUser(user);

        // 3. Kaydet
        return addressRepository.save(address);
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUser_UserId(userId);
    }
}
