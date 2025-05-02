import { TokenService } from './../../../core/services/token.service';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Product } from '../../../models/product.model';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WishlistService {
  private apiUrl = 'http://localhost:8080/api/user/wishlist'; // ✅ doğru URL

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  getWishlist(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  addToWishlist(productId: number): Observable<void> {
    return this.http.post<void>(this.apiUrl, { productId });
  }

  // src/app/features/user/services/wishlist.service.ts

  removeFromWishlist(productId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}?productId=${productId}`, {
      headers: new HttpHeaders({
        Authorization: `Bearer ${this.tokenService.getToken()}`
      })
    });
  }



}
