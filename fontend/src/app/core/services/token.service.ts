import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private tokenSubject = new BehaviorSubject<string | null>(this.getTokenFromStorage());
  public token$ = this.tokenSubject.asObservable(); // dışarıdan dinlenebilir

  private getTokenFromStorage(): string | null {
    return typeof window !== 'undefined' ? localStorage.getItem('token') : null;
  }

  getToken(): string | null {
    return this.tokenSubject.value;
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
    this.tokenSubject.next(token); // yay!
  }

  removeToken(): void {
    localStorage.removeItem('token');
    this.tokenSubject.next(null); // temizle!
  }
}
