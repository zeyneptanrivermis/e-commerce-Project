import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';  // AuthService'e ihtiyacımız var

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/user';  // <-- düzeltildi

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {}

  getUserInfo(): Observable<any> {
    const headers = this.authService.getAuthHeaders();
    return this.http.get(`${this.API_URL}/info`, { headers });  // <-- info endpointi sabit kaldı
  }

  updateUser(userData: any): Observable<any> {
    const token = this.authService.getToken();
    if (!token) {
      throw new Error("JWT token bulunamadı");
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  
    return this.http.put(`${this.API_URL}/update`, userData, { headers });
  }
}

