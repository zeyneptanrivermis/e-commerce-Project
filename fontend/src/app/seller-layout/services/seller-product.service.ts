import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../models/product.model';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SellerProductService {
  private apiUrl = 'http://localhost:8080/api/products/seller';

  constructor(private http: HttpClient) {}

  getSellerProducts(sellerId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/${sellerId}`);
  }
  addProduct(product: Product, sellerId: number): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}/${sellerId}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
  getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

}
