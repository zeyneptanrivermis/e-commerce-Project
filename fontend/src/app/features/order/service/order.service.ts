import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Order } from '../../../models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  // Sepetten sipariş oluşturur
  createOrder(): Observable<Order> {
    return this.http.post<Order>(`${this.apiUrl}/create`, {});
  }

  // Kullanıcının siparişlerini getirir
  getCustomerOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }

  // Siparişe ödeme ekler
  addPayment(orderId: number, amount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${orderId}/payment?amount=${amount}`, {});
  }

  // Siparişe kargo bilgisi ekler
  addShipping(orderId: number, shippingData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/${orderId}/shipping`, shippingData);
  }
}
