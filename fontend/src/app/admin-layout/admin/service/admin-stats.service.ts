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

  constructor() { }

  getStats(): Observable<{ userCount: number, productCount: number, pendingOrders: number }> {
    // TODO: gerçek API call yap
    return of({ userCount: 120, productCount: 58, pendingOrders: 14 });
  }
}
