import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../../../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiUrl = 'http://localhost:8080/api/user'; // Backend API'si

  constructor(private http: HttpClient) { }

  // Kullanıcının wishlist'ini alma
  getWishlist(wishListId: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/wishlist/${wishListId}`);
  }

  // Wishlist'e ürün ekleme
  addToWishlist(wishListId: string, product: Product): Observable<any> {
    return this.http.post(`${this.apiUrl}/wishlist/add`, { wishListId, product });
  }

  // Wishlist'ten ürün silme
  removeFromWishlist(wishListId: string, product: Product): Observable<any> {
    return this.http.delete(`${this.apiUrl}/wishlist/remove`, { params: { wishListId, productId: product.id } });
  }
}
