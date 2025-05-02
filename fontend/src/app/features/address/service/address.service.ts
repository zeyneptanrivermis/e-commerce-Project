// src/app/services/address.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

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
}
