import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(private http: HttpClient) {}

  /**
   * Siparişe ödeme ekler: orderId path variable, amount request param
   */
  addPayment(orderId: number, amount: number): Observable<PaymentResponse> {
    const url    = `/api/orders/${orderId}/payment`;
    const params = new HttpParams().set('amount', amount.toString());
    return this.http.post<PaymentResponse>(url, null, { params });
  }
}
