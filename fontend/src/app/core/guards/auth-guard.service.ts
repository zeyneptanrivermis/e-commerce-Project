import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const requiredRoles = route.data['roles'] || ['ROLE_USER']; // Varsayılan olarak ROLE_USER
    const userRoles = this.authService.getUserRoles();
  
    if (this.authService.isLoggedIn() && requiredRoles.some((role: string) => userRoles.includes(role))) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
