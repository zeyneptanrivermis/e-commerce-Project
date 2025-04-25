import { Injectable } from '@angular/core';
import { Product } from '../../../models/product.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';


export interface CartItem {
  product: Product;
  quantity: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = 'http://localhost:8080/api/cart';

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token'); // JWT token localStorage'da saklanıyor varsayıyorum
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  addToCart(productId: number, quantity: number): Observable<string> {
    const headers = this.getAuthHeaders();
    const body = new URLSearchParams();
    body.set('productId', productId.toString());
    body.set('quantity', quantity.toString());

    return this.http.post<string>(`${this.apiUrl}/add`, body.toString(), {
      headers: headers.append('Content-Type', 'application/x-www-form-urlencoded')
    });
  }

  removeFromCart(productId: number): Observable<string> {
    const headers = this.getAuthHeaders();
    return this.http.delete<string>(`${this.apiUrl}/remove`, {
      headers: headers,
      params: { productId: productId.toString() }
    });
  }

  listCartItems(): Observable<CartItem[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<CartItem[]>(`${this.apiUrl}/items`, { headers });
  }

  getCartTotal(): Observable<number> {
    const headers = this.getAuthHeaders();
    return this.http.get<number>(`${this.apiUrl}/total`, { headers });
  }
}

