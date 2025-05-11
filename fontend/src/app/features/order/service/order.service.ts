import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Order } from '../../../models/order.model';
import { Payment } from '../../../models/payment.model';
import { TokenService } from '../../../core/services/token.service';
import { isPlatformBrowser } from '@angular/common';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private baseUrl = `${environment.apiUrl}/api/orders`;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  /**
   * Backend'deki @PostMapping("/create") endpointine POST atar.
   * Body gönderilmez, kullanıcıya göre sipariş oluşturulur.
   */
  createOrder(): Observable<Order> {
    return this.http.post<Order>(
      `${this.baseUrl}/create`,
      {} // boş body
    );
  }

  /**
   * Kullanıcının siparişlerini getirir.
   */
  getUserOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/user`);
  }

  /**
   * Siparişe ödeme ekler.
   */
  addPayment(orderId: number, amount: number): Observable<Payment> {
    const url    = `${this.baseUrl}/${orderId}/payment`;
    const params = new HttpParams().set('amount', amount.toString());
    return this.http.post<Payment>(url, null, { params });
  }

  refundOrder(orderId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${orderId}/refund`, {});
  }

  getOrders(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }
  /**
   * Siparişe kargo bilgisi ekler.
   */
  addShipping(orderId: number, shippingInfo: any): Observable<any> {
    const url = `${this.baseUrl}/${orderId}/shipping`;
    return this.http.post<any>(url, shippingInfo);
  }
}
