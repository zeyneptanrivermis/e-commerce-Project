// src/app/core/services/token.service.ts
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

getToken(): string | null {
  if (isPlatformBrowser(this.platformId)) {
    const token = localStorage.getItem('token');
    console.log('TokenService: token =', token);
    return token;
  }

  console.log('TokenService: Not in browser');
  return null;
}

  setToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('token', token);
    }
  }

  removeToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('token');
    }
  }
}
