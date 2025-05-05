import { isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { AuthService } from './features/auth/services/auth.service';
import { TokenService } from './core/services/token.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent implements OnInit {
  title = 'e-commerceWebsite';
  userRoles: string[] = [];

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.authService.tokenService.token$.subscribe(token => {
      if (token) {
        this.userRoles = this.authService.getUserRoles();
        console.log('🎭 Roller (login sonrası):', this.userRoles);
      } else {
        this.userRoles = [];
        console.log('🚫 Oturum yok, roller sıfırlandı.');
      }
    });
  }
  

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn(); // Artık localStorage erişimini servis yönetsin
  }
}
