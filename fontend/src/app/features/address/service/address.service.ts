// src/app/services/address.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Address } from '../../../models/Address.model';

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  private baseUrl = 'http://localhost:8080/api/addresses';

  constructor(private http: HttpClient) { }

  addAddress(userId: number, address: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/user/${userId}`, address);
  }

  getUserAddresses(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`);
  }
  deleteAddress(addressId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${addressId}`);
  }
  updateAddress(addressId: number, updatedData: Address): Observable<Address> {
    return this.http.put<Address>(`${this.baseUrl}/${addressId}`, updatedData);
  }


}
