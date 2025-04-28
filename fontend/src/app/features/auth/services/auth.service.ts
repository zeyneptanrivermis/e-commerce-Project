import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth'; // Backend Auth Endpoint
  private readonly TOKEN_KEY = 'token'; // JWT token key

  constructor(private http: HttpClient) {}

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
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem(this.TOKEN_KEY);
    } else {
      return null;  // Return null if localStorage is not available
    }
  }

  // Manually decode JWT
  getCurrentUser(): any {
    const token = this.getToken();
    if (token) {
      return decodeJWT(token); // Decode edilen token'dan kullanıcı bilgisini al
    } else {
      return null;
    }
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return token !== null;  // Check if token exists
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
  // Utility function to decode base64url encoded strings
  function base64UrlDecode(base64Url: string): string {
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/'); // Base64 url safe'den standart base64'e dönüştür
    const decodedString = atob(base64);  // Base64'ü çöz
    return decodedString;
  }
  
  // Decode JWT Token manually
  function decodeJWT(token: string): any {
    if (!token) {
      return null;
    }
    console.log(token);
    // Split the token into 3 parts (Header, Payload, Signature)
    const parts = token.split('.');
  
    if (parts.length !== 3) {
      console.error('Invalid token structure');
      return null;
    }
  
    // Decode the Payload (second part)
    const payload = base64UrlDecode(parts[1]);
  
    try {
      // Parse the JSON object from the decoded payload
      return JSON.parse(payload);
    } catch (error) {
      console.error('Error parsing JWT payload:', error);
      return null;
    }
  }