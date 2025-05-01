import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from '../../../models/product.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  private apiUrl = 'http://localhost:8080/api/user/wishlist';

  constructor(private http: HttpClient) {}

  getWishlist(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  addToWishlist(productId: number): Observable<void> {
    return this.http.post<void>(this.apiUrl, { productId });
  }

  removeFromWishlist(customerId: number, productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${customerId}/remove/${productId}`);
  }
}
