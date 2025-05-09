import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthService } from '../../features/auth/services/auth.service';
import { Router } from '@angular/router';
import { TokenService } from '../../core/services/token.service';

@Injectable({
  providedIn: 'root'
})
export class SellerAuthService {
  private apiUrl = 'http://localhost:8080/api/seller/auth';

  constructor(private http: HttpClient, private authService: AuthService,
    private router: Router,  private tokenService: TokenService
  ) {}
  
  register(data: {
    name: string;
    surname: string;
    shopName: string;
    email: string;
    password: string;
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, data);
  }

  canActivate(): boolean {
    const roles = this.authService.getUserRoles();

    const isLoggedIn = this.authService.isLoggedIn();
    const isSeller = roles.includes('ROLE_SELLER');

    if (isLoggedIn && isSeller) {
      return true;
    }

    this.router.navigate(['/unauthorized']);
    return false;
  }


  login(data: { email: string; password: string }): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, data).pipe(
      tap(res => {
        this.tokenService.setToken(res.token); // ✅ burada artık çalışır
      })
    );
  }
  
}

export interface SellerRegisterRequest {
  name: string;
  surname: string;
  email: string;
  password: string;
}
