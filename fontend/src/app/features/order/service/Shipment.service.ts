import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ShipmentService {

  private apiUrl = 'http://localhost:8080/api/shipments';

  constructor(private http: HttpClient) {}

  /** Backend'den sadece status string'i dönen endpoint */
  getStatus(orderId: number): Observable<string> {
    return this.http.get<string>(`${this.apiUrl}/${orderId}/status`);
  }

  /** Check if payment has been received for the order */
  checkPaymentStatus(orderId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${orderId}/payment-status`);
  }
}
