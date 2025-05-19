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

  private apiUrl = 'http://localhost:8080/api/seller/auth';

  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<SellerDashboardDTO> {
    return this.http.get<SellerDashboardDTO>(`${this.apiUrl}/dashboard`);
  }
}

export interface TopSellingProduct {
  productName: string;
  sales: number;
  revenue: number;
}

export interface SellerDashboardDTO {
  name: string;
  email: string;
  totalProducts: number;
  totalOrders: number;
  totalSales: number;
  topSellingProducts: TopSellingProduct[];
}
