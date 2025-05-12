import { AuthService } from './../../../features/auth/services/auth.service';
import { Component, ElementRef, ViewChild } from "@angular/core";

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  @ViewChild('megaMenu') megaMenuRef!: ElementRef;
  @ViewChild('leftPane') leftPaneRef!: ElementRef;
  @ViewChild('submenu') submenuRef!: ElementRef;

  isMenuOpen = false;

  constructor(public authService:AuthService){}

  toggleMegaMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;

    const megaMenu = this.megaMenuRef.nativeElement as HTMLElement;
    const leftPane = this.leftPaneRef.nativeElement as HTMLElement;
    const submenu = this.submenuRef.nativeElement as HTMLElement;

    megaMenu.style.display = this.isMenuOpen ? 'flex' : 'none';
    leftPane.style.display = this.isMenuOpen ? 'block' : 'none';
    submenu.innerHTML = ''; // Menü her açıldığında alt içerik sıfırlansın
  }

  // Giriş yapma
  onLogin(): void {
    this.authService.login({ email: 'user@example.com', password: 'password123' }).subscribe({
      next: (response) => {
        console.log('Login response:', response);
        this.authService.saveToken(response.token);
      },
      error: (err) => {
        console.error('Login failed:', err);
      }
    });
    
  }
  

  // Çıkış yapma
  seeUserProfile(): void {
    this.authService.logout();
  }
  normalizeCategory(name: string): string {
  return name.toUpperCase().replace(/\s+/g, '_').replace(/&/g, 'AND');
}

 
}

