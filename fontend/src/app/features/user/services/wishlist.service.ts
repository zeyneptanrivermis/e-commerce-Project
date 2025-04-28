import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../../../models/product.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  private apiUrl = 'http://localhost:8080/api/wishlist';

  constructor(private http: HttpClient) {}

  getWishlist(customerId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/${customerId}`);
  }

  addToWishlist(customerId: number, productId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${customerId}/add/${productId}`, {});
  }

  removeFromWishlist(customerId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${customerId}/remove/${productId}`);
  }
}