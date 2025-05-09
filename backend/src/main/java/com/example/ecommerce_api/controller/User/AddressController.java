package com.example.ecommerce_api.controller.User;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce_api.dto.UserDTO.AddressDTO;
import com.example.ecommerce_api.entity.UserEntity.Address;
import com.example.ecommerce_api.repository.UserRepositories.AddressRepository;
import com.example.ecommerce_api.services.User.AddressService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:4200") // Angular'dan gelen istekler için
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @PostMapping("/user/{userId}")
    public ResponseEntity<Address> addAddressToUser(@PathVariable Long userId, @RequestBody Address address) {
        Address saved = addressService.addAddressToUser(userId, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
        Address existingAddress = addressRepository.findById(addressId)
            .orElseThrow(() -> new EntityNotFoundException("Address not found: " + addressId));

        existingAddress.setCountry(updatedAddress.getCountry());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setDistrict(updatedAddress.getDistrict());
        existingAddress.setAddressDetail(updatedAddress.getAddressDetail());

        Address saved = addressRepository.save(existingAddress);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/addresses/user/{userId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByUserId(@PathVariable Long userId) {
        List<Address> addresses = addressRepository.findByUser_UserId(userId);
        List<AddressDTO> dtoList = addresses.stream().map(AddressDTO::new).toList();
        return ResponseEntity.ok(dtoList);
    }
}

