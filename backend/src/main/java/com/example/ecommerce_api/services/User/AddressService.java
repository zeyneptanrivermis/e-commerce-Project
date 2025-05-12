package com.example.ecommerce_api.services.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce_api.dto.OrderDTO.RefundResponse;
import com.example.ecommerce_api.entity.UserEntity.Address;
import com.example.ecommerce_api.entity.UserEntity.User;
import com.example.ecommerce_api.repository.UserRepositories.AddressRepository;
import com.example.ecommerce_api.repository.UserRepositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public Address addAddressToUser(Long userId, Address address) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı. ID: " + userId));

        address.setUser(user);
        return addressRepository.save(address);
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUser_UserId(userId);
    }

    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));
        addressRepository.delete(address);
    }

    public RefundResponse approveRefund(Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveRefund'");
    }
    

}