import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Product } from "../../models/product.model";
import { Order } from "../../models/order.model";
import { environment } from "../../../environments/environment";

export interface User {
  userId: number;
  name: string;
  email: string;
  roles: string[];
  banned: boolean;
}
/*
export interface Product {
  id: number;
  name: string;           // ✅ JSON'daki "name" ile eşleşir
  price: number;
  cancelled: boolean;
}*/

@Injectable({ providedIn: 'root' })
export class AdminApiService {
  private base = '/api/admin';
  private baseUrl = `${environment.apiUrl}/api`;

  constructor(private http: HttpClient) {}

  // --- Customers ---
  getAllCustomers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.base}/customers`);
  }

  toggleCustomerBan(id: number): Observable<void> {
    return this.http.put<void>(`${this.base}/customers/${id}/ban`, null);
  }

  updateCustomer(id: number, data: User): Observable<any> {
    return this.http.put(`${this.base}/customers/${id}`, data);
  }

  // --- Sellers ---
  getAllSellers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.base}/sellers`);
  }

  toggleSellerBan(id: number): Observable<void> {
    return this.http.put<void>(`${this.base}/sellers/${id}/ban`, null);
  }

  updateSeller(id: number, data: User): Observable<void> {
    return this.http.put<void>(`${this.base}/sellers/${id}`, data);
  }

  // --- Products ---
  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.base}/products`);
  }

  cancelProduct(id: number): Observable<void> {
    return this.http.put<void>(`${this.base}/products/${id}/cancel`, null);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/products/${id}/delete`);
  }

  // --- Orders ---
  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.baseUrl}/orders/admin/all`);
  }

  updateOrderStatus(orderId: number, status: string): Observable<Order> {
    return this.http.put<Order>(`${this.baseUrl}/orders/admin/${orderId}/status?status=${status}`, {});
  }
}
