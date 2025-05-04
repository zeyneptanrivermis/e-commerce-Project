
package com.example.ecommerce_api.dto.UserDTO;

import com.example.ecommerce_api.entity.UserEntity.Address;

public class AddressDTO {
    private Long id;
    private String country;
    private String city;
    private String district;
    private String addressDetail;

    public AddressDTO(Address address) {
        this.id = address.getAddressId();
        this.country = address.getCountry();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.addressDetail = address.getAddressDetail();
    }

    // Getter'lar
    public Long getId() { return id; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public String getDistrict() { return district; }
    public String getAddressDetail() { return addressDetail; }
}
