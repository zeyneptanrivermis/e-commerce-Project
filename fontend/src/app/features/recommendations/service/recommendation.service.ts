import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Recommendation } from '../../../models/recommendation.model';

@Injectable({
  providedIn: 'root'
})
export class RecommendationService {

  private readonly apiUrl = '/api/recommendations';

  constructor(private http: HttpClient) {}

  /**
   * Kullanıcı-temelli öneriler
   */
  getRecommendations(userId: number, limit: number = 5): Observable<Recommendation[]> {
    const params = new HttpParams()
      .set('userId', userId.toString())
      .set('limit', limit.toString());
    return this.http.get<Recommendation[]>(this.apiUrl, { params });
  }

  /**
   * Ürün-temelli benzer ürünler
   */
  getSimilarProducts(productId: number, limit: number = 5): Observable<Recommendation[]> {
    const params = new HttpParams()
      .set('productId', productId.toString())
      .set('limit',     limit.toString());
    return this.http.get<Recommendation[]>(`${this.apiUrl}/similar`, { params });
  }
}
