import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../../models/order.model';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = 'http://localhost:8080/api/orders';

  constructor(
    private http: HttpClient,
    private authService: AuthService  // AuthService'i inject et
  ) {}

  // Sipariş gönderme
  placeOrder(order: Order): Observable<any> {
    const headers = this.authService.getAuthHeaders();  // Authorization header'ı ekliyoruz
    return this.http.post(`${this.baseUrl}`, order, { headers });  // apiUrl hatalıydı, baseUrl kullanılmalı
  }

  // Kullanıcının tüm siparişlerini getirme
  getUserOrders(userId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/user/${userId}`);
  }

  // Tekil siparişi ID ile alma
  getOrderById(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.baseUrl}/${orderId}`);
  }

  // Siparişi iptal etme
  cancelOrder(orderId: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/cancel/${orderId}`, null);
  }
}
