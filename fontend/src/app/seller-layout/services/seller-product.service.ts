import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../models/product.model';
@Injectable({
  providedIn: 'root'
})
export class SellerProductService {
  private apiUrl = 'http://localhost:8080/api/seller/products';

  constructor(private http: HttpClient) {}

  getSellerProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }
}
