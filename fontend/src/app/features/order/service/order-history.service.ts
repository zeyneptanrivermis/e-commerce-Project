import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderHistory } from '../../../models/OrderHistory.model';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {
  private baseUrl = '/api/orders/user';

  constructor(private http: HttpClient) {}

  getHistory(): Observable<OrderHistory[]> {
    return this.http.get<OrderHistory[]>(`${this.baseUrl}/history`);
  }
}
