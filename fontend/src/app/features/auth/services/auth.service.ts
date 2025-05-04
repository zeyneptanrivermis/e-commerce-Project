import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../../models/user.model';

@Injectable({
  providedIn: 'root'
})

export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth'; // Backend Auth Endpoint
  private readonly TOKEN_KEY = 'token'; // JWT token key

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  login(email: string, password: string): Observable<any> {
    const loginData = { email, password };
    return this.http.post(`${this.API_URL}/login`, loginData);
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, userData);
  }

  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');
    }
    return null;
  }



  getCurrentUser(): User | null {
    if (!isPlatformBrowser(this.platformId)) return null;

    const token = localStorage.getItem('token');
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

  getUserRoles(): string[] {
    const token = this.getToken();
    if (!token) return [];
  
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.roles || [];
    } catch (e) {
      console.error('❌ Rol parse hatası:', e);
      return [];
    }
  }
  

  isLoggedIn(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      return token !== null;
    }
    return false; // SSR ortamında localStorage erişilemez
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

}



