import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../models/product.model';
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

}
