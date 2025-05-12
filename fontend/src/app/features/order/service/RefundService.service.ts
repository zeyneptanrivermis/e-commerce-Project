import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RefundResponse } from '../../../models/RefundResponse.model';

@Injectable({
  providedIn: 'root'
})
export class RefundServiceService {
    private baseUrl = '/api/orders';

  constructor(private http: HttpClient) {}

  /** Şimdi sadece orderId ile çağırılıyor */
  requestRefund(orderId: number): Observable<RefundResponse> {
    return this.http.post<RefundResponse>(`${this.baseUrl}/${orderId}/refund`, {});
  }
}
