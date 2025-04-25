package com.example.ecommerce_api.entity.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


//ekleme yapilabilr
@Entity
public class Address {

    // userda onetomany kurduk burada şart
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    private String country = "Turkey"; // su an sadece turkiye icinde kargo

    @Enumerated(EnumType.STRING)
    private City city;

    @Enumerated(EnumType.STRING)
    private District district;

    public Address() {}

    public Address(City city, District district) {
        this.city = city;
        this.district = district;
    }

    public String showAddressDetails() {
        return country + ", " + city + ", " + district;
    }

    // Getter - Setter
    public Long getAddressId() {
        return addressId;
    }

    public String getCountry() {
        return country;
    }

    public City getCity() {
        return city;
    }

    public District getDistrict() {
        return district;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null && !user.getAddressList().contains(this)) {
            user.getAddressList().add(this);
        }
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public void setDistrict(District district) {
        this.district = district;
    }
}

enum City {
    ADANA,
    ADIYAMAN,
    AFYONKARAHISAR,
    AGRI,
    AMASYA,
    ANKARA,
    ANTALYA,
    ARTVIN,
    AYDIN,
    BALIKESIR,
    BILECIK,
    BINGOL,
    BITLIS,
    BOLU,
    BURDUR,
    BURSA,
    CANAKKALE,
    CANKIRI,
    CORUM,
    DENIZLI,
    DIYARBAKIR,
    EDIRNE,
    ELAZIG,
    ERZINCAN,
    ERZURUM,
    ESKISEHIR,
    GAZIANTEP,
    GIRESUN,
    GUMUSHANE,
    HAKKARI,
    HATAY,
    ISPARTA,
    MERSIN,
    ISTANBUL,
    IZMIR,
    KARS,
    KASTAMONU,
    KAYSERI,
    KIRKLARELI,
    KIRSEHIR,
    KOCAELI,
    KONYA,
    KUTAHYA,
    MALATYA,
    MANISA,
    KAHRAMANMARAS,
    MARDIN,
    MUGLA,
    MUS,
    NEVSEHIR,
    NIGDE,
    ORDU,
    RIZE,
    SAKARYA,
    SAMSUN,
    SIIRT,
    SINOP,
    SIVAS,
    TEKIRDAG,
    TOKAT,
    TRABZON,
    TUNCELI,
    SANLIURFA,
    USAK,
    VAN,
    YOZGAT,
    ZONGULDAK,
    AKSARAY,
    BAYBURT,
    KARAMAN,
    KIRIKKALE,
    BATMAN,
    SIRNAK,
    BARTIN,
    ARDAHAN,
    IGDIR,
    YALOVA,
    KARABUK,
    KILIS,
    OSMANIYE,
    DUZCE
}
enum District {

    MERKEZ(null),

    // ISTANBUL
    KADIKOY(City.ISTANBUL),
    BESIKTAS(City.ISTANBUL),

    // ANKARA
    CANKAYA(City.ANKARA),
    KECIOREN(City.ANKARA),

    // IZMIR
    KONAK(City.IZMIR),
    KARSIYAKA(City.IZMIR),

    // BURSA
    NILUFER(City.BURSA),
    OSMANGAZI(City.BURSA),

    // ANTALYA
    MURATPASA(City.ANTALYA),
    KEPEZ(City.ANTALYA);

    private final City city;

    District(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }
}
