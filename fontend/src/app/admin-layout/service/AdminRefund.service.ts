import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RefundResponse } from '../../models/RefundResponse.model';

@Injectable({
  providedIn: 'root'
})
export class AdminRefundService {
  private baseUrl = '/api/admin/orders';

  constructor(private http: HttpClient) {}

  requestRefund(orderId: number): Observable<void> {
  return this.http.post<void>(`/api/orders/${orderId}/refund`, {});
}

  /** Gerçek iade onayı: Stripe refund + statü güncelle */
  approve(orderId: number): Observable<RefundResponse> {
    return this.http.post<RefundResponse>(
      `${this.baseUrl}/${orderId}/refund-approve`, {}
    );
  }

  /** İade talebini reddet: statü rollback */
  decline(orderId: number): Observable<void> {
    return this.http.post<void>(
      `${this.baseUrl}/${orderId}/refund-decline`, {}
    );
  }

}
