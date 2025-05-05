import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { Product } from '../../../models/product.model';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';
import { TokenService } from '../../../core/services/token.service';

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

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  // JWT token'ı header'a ekleyen yardımcı metod
  private getAuthHeaders(): HttpHeaders {
    if (isPlatformBrowser(this.platformId)) {
      const token = this.tokenService.getToken();
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    } else {
      console.warn('🚫 Token erişimi sunucu tarafında engellendi (SSR)');
      return new HttpHeaders(); // SSR'de boş header döndür
    }
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
  removeFromCart(productId: number | null): Observable<string> {
    if (productId == null) {
      console.error('❌ removeFromCart: productId null!');
      throw new Error('Product ID is null. Cannot remove item.');
    }

    const headers = this.getAuthHeaders();
    const params = new HttpParams().set('productId', productId.toString());

    return this.http.delete<string>(`${this.apiUrl}/remove`, {
      headers,
      params
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

  updateQuantity(productId: number, quantity: number): Observable<any> {
    const headers = this.getAuthHeaders();
    const params = new HttpParams()
      .set('productId', productId)
      .set('quantity', quantity);

    return this.http.put(`${this.apiUrl}/update`, null, {
      headers,
      params,
      responseType: 'text'  // 🔥 bu satır kilit
    });
  }

  /** Sepetteki tüm öğeleri siler */
  clearCart(): Observable<any[]> {
    return this.listCartItems().pipe(
      switchMap(items => {
        if (items.length === 0) {
          return of([]);                    // Sepet boşsa hemen tamam
        }
        // Tüm silme isteklerini paralel çalıştır
        return forkJoin(items.map(item =>
          this.removeFromCart(item.product.productId)
        ));
      })
    );
  }

}


