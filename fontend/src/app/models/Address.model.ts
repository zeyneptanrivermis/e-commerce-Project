export interface Address {
    addressId: number;
    country: string;
    city: string;
    district: string;
  }

export enum City{
    ANKARA = 'ANKARA',
    ISTANBUL = 'ISTANBUL',
    IZMIR = 'IZMIR',
    BURSA = 'BURSA',
    ANTALYA = 'ANTALYA',
    // Devamını enum'dan kopyala
}

export enum District{
    MERKEZ = 'MERKEZ',
    KADIKOY = 'KADIKOY',
    BESIKTAS = 'BESIKTAS',
    CANKAYA = 'CANKAYA',
    KECIOREN = 'KECIOREN',
    KONAK = 'KONAK',
    KARSIYAKA = 'KARSIYAKA',
    NILUFER = 'NILUFER',
    OSMANGAZI = 'OSMANGAZI',
    MURATPASA = 'MURATPASA',
    KEPEZ = 'KEPEZ'
}

export const CITY_DISTRICT_MAP: { [key in City]?: District[] } = {
    [City.ISTANBUL]: [District.KADIKOY, District.BESIKTAS],
    [City.ANKARA]: [District.CANKAYA, District.KECIOREN],
    [City.IZMIR]: [District.KONAK, District.KARSIYAKA],
    [City.BURSA]: [District.NILUFER, District.OSMANGAZI],
    [City.ANTALYA]: [District.MURATPASA, District.KEPEZ]
  };