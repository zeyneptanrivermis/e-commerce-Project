import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OrderHistory } from '../../../models/OrderHistory.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderHistoryService {

  private baseUrl = `${environment.apiUrl}/api/orders/user`;
  constructor(private http: HttpClient) {}

  getHistory(): Observable<OrderHistory[]> {
    return this.http.get<OrderHistory[]>(`${this.baseUrl}/history`);
  }
}
