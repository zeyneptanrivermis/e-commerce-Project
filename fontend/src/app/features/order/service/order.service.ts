import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../../models/order.model';
import { AuthService } from '../../auth/services/auth.service';
import { Payment } from '../../../models/payment.model';
import { TokenService } from '../../../core/services/token.service';
import { isPlatformBrowser } from '@angular/common';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = 'http://localhost:8080/api/orders';
  
  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}
  
  placeOrder(order: Order): Observable<any> {
    let headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  
    if (isPlatformBrowser(this.platformId)) {
      const token = this.tokenService.getToken();
      if (token) {
        headers = headers.set('Authorization', `Bearer ${token}`);
      } else {
        console.warn('❗ Token boş, yetkisiz işlem olabilir.');
      }
    } else {
      console.warn('🚫 Token erişimi sunucu tarafında engellendi (SSR)');
    }
  
    return this.http.post<any>('http://localhost:8080/api/orders/place', order, { headers });
  }

  getUserOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/user`);
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
