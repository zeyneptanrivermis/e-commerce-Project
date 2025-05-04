import { isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { AuthService } from './features/auth/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone:false
})
export class AppComponent {
  title = 'e-commerceWebsite';
  userRoles: string[] = []; // Holds the roles of the logged-in user

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthService
  ) {
    if (this.isLoggedIn) {
      this.userRoles = this.authService.getUserRoles(); // Fetch user roles if logged in
    }
  }
  
  ngOnInit(): void {
    if (this.isLoggedIn) {
      this.userRoles = this.authService.getUserRoles(); // Kullanıcı rollerini al
    }
  }
  get isLoggedIn(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      return token !== null;
    }
    return false; // Return false during server-side rendering (SSR)
  }

}
