import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../../models/order.model';
import { AuthService } from '../../auth/services/auth.service';
import { Payment } from '../../../models/payment.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = 'http://localhost:8080/api/orders';

  constructor(
    private http: HttpClient,
    private authService: AuthService  // AuthService'i inject et
  ) {}

  // Sipariş gönderme
  placeOrder(order: Order): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    });

    return this.http.post(`${this.baseUrl}/create`, order, { headers });
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

  addPayment(orderId: number, amount: number): Observable<Payment> {
    const url    = `${this.baseUrl}/${orderId}/payment`;
    const params = new HttpParams().set('amount', amount.toString());
    return this.http.post<Payment>(url, null, { params });
  }

}
