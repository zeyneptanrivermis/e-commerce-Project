import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../../../environments/environment';

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

  // 1) Ödeme intent yarat
  createPaymentIntent(orderId: number, currency: string): Observable<{ clientSecret: string }> {
    return this.http.post<{ clientSecret: string }>(
      `${environment.apiUrl}/api/payments/create-intent/${orderId}`,
      { currency }
    );
  }

  // 2) Backend'e ödeme tamamlandı bildir
  completePayment(orderId: number, paymentIntentId: string): Observable<void> {
    return this.http.post<void>(
      `${environment.apiUrl}/api/payments/complete/${orderId}`,
      { paymentIntentId }
    );
  }

}
