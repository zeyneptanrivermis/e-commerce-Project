import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../../../../models/product.model';

@Injectable({
  providedIn: 'root'
})

// manage-product.service.ts
@Injectable({ providedIn: 'root' })
export class ManageProductService {
  private apiUrl = 'http://localhost:8080/api/seller/auth/products';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(`${this.apiUrl}`, product, { headers: this.getHeaders() });
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product, { headers: this.getHeaders() });
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }

  getSellerProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}`, { headers: this.getHeaders() });
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
  }
}
