import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { of, Observable } from 'rxjs';

export interface Stats {
  userCount: number;
  productCount: number;
  pendingOrders: number;
}

@Injectable({
  providedIn: 'root'
})
export class AdminStatsService {

  private readonly baseUrl = '/api/admin/stats';

  constructor(private http: HttpClient) {}

  getStats(): Observable<Stats> {
    return this.http.get<Stats>(this.baseUrl);
  }
}
