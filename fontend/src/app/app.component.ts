import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser }                     from '@angular/common';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { filter }                                from 'rxjs/operators';
import { AuthService }                           from './features/auth/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: false,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  userRoles: string[] = [];
  dataLoaded = false;
  isAdmin = false;
  showLayout = true;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {}

  get isLoggedIn(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return !!localStorage.getItem('token');
    }
    return false;
  }

  ngOnInit(): void {
    // Roller ve login durumunu hazırla
    if (this.isLoggedIn) {
      this.userRoles = this.authService.getUserRoles();
      this.isAdmin    = this.userRoles.includes('ROLE_ADMIN');
    }
    this.dataLoaded = true;

    // Her navigasyon sonrası hem root hem çocuk data.hideLayout kontrolü
    this.router.events.pipe(
      filter(evt => evt instanceof NavigationEnd)
    ).subscribe(() => {
      let route = this.activatedRoute.root;
      let hide = false;

      // En üstten başlayıp tüm child’lara iniyoruz
      while (route) {
        const data = route.snapshot.data;
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
