import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User } from '../../../models/user.model';
import { TokenService } from '../../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    public tokenService: TokenService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  
  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => {
        this.tokenService.setToken(response.token); // ✔️ Tek merkezden set et
      })
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, userData);
  }

  getToken(): string | null {
    return this.tokenService.getToken(); // ✔️ localStorage erişimi içeride güvenli
  }

  saveToken(token: string): void {
    this.tokenService.setToken(token); // Yalnızca delegasyon yap
  }
  

  getCurrentUser(): User | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        userId: payload.userId,
        name: payload.name,
        surname: payload.surname,
        email: payload.email,
        birthday: payload.birthday,
        wishListId: payload.wishListId
      };
    } catch (e) {
      console.error('❌ Token parse hatası:', e);
      return null;
    }
  }

  isLoggedIn(): boolean {
    return this.tokenService.getToken() !== null;
  }
  
  getUserRoles(): string[] {
    const token = this.getToken(); // tokenService.getToken()
    if (!token) return [];
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.roles || [];
    } catch {
      return [];
    }
    
  }
  
  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  logout(): void {
    this.tokenService.removeToken(); // ✔️ tek merkezden sil
  }
}
