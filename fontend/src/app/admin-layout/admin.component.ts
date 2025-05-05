import { Router } from '@angular/router';
import { Component } from '@angular/core';
import { AuthService } from '../features/auth/services/auth.service';

@Component({
  selector: 'app-admin',
  standalone: false,
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {


  tab: string = 'dashboard';

  constructor(private authService:AuthService, private router: Router){}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/home']).then(() => {
      setTimeout(() => {
        window.location.reload();
      }, 100);
    });
  }

  setTab(tabName: string) {
    this.tab = tabName;
  }
}

