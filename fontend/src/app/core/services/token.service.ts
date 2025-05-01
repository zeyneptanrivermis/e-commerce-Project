// src/app/core/services/token.service.ts
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  getToken(): string | null {
    console.log('Running in browser?', isPlatformBrowser(this.platformId));
    console.log('Token in TokenService:', localStorage.getItem('token'));
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');

    }
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
