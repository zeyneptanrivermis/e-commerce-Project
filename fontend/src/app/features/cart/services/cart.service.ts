import { Injectable } from '@angular/core';
import { Product } from '../../../models/product.model';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface CartItem {
  cartItemId: number;
  quantity: number;
  totalPrice: number;
  product: {
    productId: number;
    name: string;
    price: number;
  };
}

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = 'http://localhost:8080/api/cart';

  constructor(private http: HttpClient) { }

  // JWT token'ı header'a ekleyen yardımcı metod
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }


  // Sepete ürün ekleme
  // Sepete ürün ekleme (Authorization header eklendi)
  addToCart(productId: number, quantity: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.apiUrl}/add`, {
      productId: productId,
      quantity: quantity
    }, { headers }).pipe(
      catchError(this.handleError)
    );
  }


  // Sepetten ürün silme
  removeFromCart(productId: number): Observable<string> {
    const headers = this.getAuthHeaders();
    const params = new HttpParams().set('productId', productId.toString());

    return this.http.delete<string>(`${this.apiUrl}/remove`, {
      headers: headers,
      params: params
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Sepet öğelerini listeleme
  listCartItems(): Observable<CartItem[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<CartItem[]>(`${this.apiUrl}/items`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Sepet toplamını alma
  getCartTotal(): Observable<number> {
    const headers = this.getAuthHeaders();
    return this.http.get<number>(`${this.apiUrl}/total`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  // Hata işlemi için yardımcı metod
  private handleError(error: any): Observable<never> {
    console.error('Error occurred:', error);
    throw error;
  }

  updateQuantity(productId: number, quantity: number): Observable<string> {
    const headers = this.getAuthHeaders();
    const params = new HttpParams()
      .set('productId', productId)
      .set('quantity', quantity);

    return this.http.put<string>(`${this.apiUrl}/update`, null, {
      headers,
      params
    }).pipe(
      catchError(this.handleError)
    );
  }
}
