import { Injectable } from '@angular/core';
import { Product } from '../../../../models/product.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PopularityService {
  private popularityMap = new Map<number, number>(); // productId → count
  private apiUrl = 'http://localhost:8080/api/products'; // 🔧 düzeltildi

  constructor(private http: HttpClient){}

  increment(productId: number): void {
    const current = this.popularityMap.get(productId) || 0;
    this.popularityMap.set(productId, current + 1);
  }

  getPopularProducts(count: number = 10): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/popular?count=${count}`);
  }

}
