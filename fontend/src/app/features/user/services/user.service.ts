import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';  // AuthService'e ihtiyacımız var

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/user/info';  // Kullanıcı bilgisi için backend URL

  constructor(
    private http: HttpClient,
    private authService: AuthService  // AuthService'i burada kullanıyoruz
  ) {}

  // Kullanıcı bilgilerini almak için HTTP GET isteği
  getUserInfo(): Observable<any> {
    const headers = this.authService.getAuthHeaders();  // Authorization header'ını alıyoruz
    return this.http.get(this.API_URL, { headers });  // Kullanıcı bilgilerini backend'den alıyoruz
  }
  
  updateUser(userData: any): Observable<any> {
    const headers = this.authService.getAuthHeaders();  // Tokenı ekliyoruz
    return this.http.put('http://localhost:8080/api/user/update', userData, { headers });
  }
}
