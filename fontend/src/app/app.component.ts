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
  dataLoaded = false;
  isAdmin = false;
  showLayout = true;

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
    // 1) Token değişimlerini dinle
    this.tokenService.token$.subscribe(token => {
      if (token) {
        this.userRoles = this.authService.getUserRoles();
        this.isAdmin    = this.userRoles.includes('ROLE_ADMIN');
        console.log('🎭 Roller (login sonrası):', this.userRoles);
      } else {
        this.userRoles = [];
        this.isAdmin    = false;
        console.log('🚫 Oturum yok, roller sıfırlandı.');
      }
      this.dataLoaded = true;
    });

    // 2) Route değişimlerinde layout kontrolü
    this.router.events.pipe(
      filter(evt => evt instanceof NavigationEnd)
    ).subscribe(() => {
      let route = this.activatedRoute.root;
      let hide = false;

      // En üstten başlayıp tüm nested route’ları dolaş
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
