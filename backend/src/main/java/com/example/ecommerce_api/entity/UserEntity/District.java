package com.example.ecommerce_api.entity.UserEntity;

public enum District {

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

