import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReviewRequestDTO } from '../../../../models/ReviewRequestDTO';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) {}

  /** Ürüne yorum yapmaya uygun mu? */
  canReview(productId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/can-review/${productId}`);
  }

  /** Yeni yorumu backend’e gönder */
  createReview(dto: ReviewRequestDTO): Observable<any> {
    return this.http.post(`${this.apiUrl}`, dto);
  }

}
