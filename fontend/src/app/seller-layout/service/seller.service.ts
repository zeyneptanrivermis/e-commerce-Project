import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SellerDashboardDTO {
  id: number;
  name: string;
  email: string;
  totalProducts: number;
  totalOrders: number;
}

@Injectable({
  providedIn: 'root'
})
export class SellerService {

  private apiUrl = 'http://localhost:8080/api/seller';

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<SellerDashboardDTO> {
    return this.http.get<SellerDashboardDTO>(`${this.apiUrl}/dashboard`);
  }
}
