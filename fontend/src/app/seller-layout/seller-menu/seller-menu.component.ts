import { Component } from '@angular/core';
import { AuthService } from '../../features/auth/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-seller-menu',
  standalone: false,
  templateUrl: './seller-menu.component.html',
  styleUrl: './seller-menu.component.css'
})
export class SellerMenuComponent {

  constructor(private authService: AuthService, private router: Router) {}

  logout(){
    this.authService.logout();
    this.router.navigate(['/home']);
  }
}
