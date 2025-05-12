import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser }                     from '@angular/common';
import {
  Router,
  NavigationEnd,
  ActivatedRoute,
  Data
} from '@angular/router';
import { filter }                                from 'rxjs/operators';

import { AuthService }   from './features/auth/services/auth.service';
import { TokenService }  from './core/services/token.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  standalone: false
})
export class AppComponent implements OnInit {
  title = 'e-commerceWebsite';
  userRoles: string[] = [];
  dataLoaded = true;
  isAdmin = false;
  showLayout = true;
  isSeller = false;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthService,
    private tokenService: TokenService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  get isLoggedIn(): boolean {
    // localStorage kontrolünü servis yönetsin
    return this.authService.isLoggedIn();
  }


  ngOnInit(): void {
    // Set dataLoaded to true immediately if token is already present
    if (this.tokenService.getToken()) {
      this.userRoles = this.authService.getUserRoles();
      this.isAdmin   = this.userRoles.includes('ROLE_ADMIN');
      this.isSeller  = this.userRoles.includes('ROLE_SELLER');
      this.dataLoaded = true;
    }

    this.tokenService.token$.subscribe(token => {
      if (token) {
        this.userRoles = this.authService.getUserRoles();
        this.isAdmin   = this.userRoles.includes('ROLE_ADMIN');
        this.isSeller  = this.userRoles.includes('ROLE_SELLER');
  
        console.log('🎭 Roller:', this.userRoles);
  
        // Oturum açıldıktan sonra ilk yüklemede yönlendirme yap
        if (this.isSeller && this.router.url === '/login') {
          this.router.navigate(['/seller/dashboard']);
        } else if (this.isAdmin && this.router.url === '/login') {
          this.router.navigate(['/admin']);
        }
      } else {
        this.userRoles = [];
        this.isAdmin = false;
        this.isSeller = false;
        console.log('🚫 Oturum yok.');
      }
  
      this.dataLoaded = true;
    });
  
    // Route değişimlerinde layout kontrolü
    this.router.events.pipe(
      filter(evt => evt instanceof NavigationEnd)
    ).subscribe(() => {
      let route = this.activatedRoute.root;
      let hide = false;
  
      while (route) {
        const data: Data = route.snapshot.data;
        if (data && data['hideLayout']) {
          hide = true;
          break;
        }
        route = route.firstChild!;
      }
  
      this.showLayout = !hide;
    });
  }
  
}
