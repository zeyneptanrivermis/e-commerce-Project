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
    this.authService.login('user@example.com', 'password123').subscribe({
      next: (response) => {
        console.log('Login response:', response);  // Gelen JWT'yi kontrol et
        this.authService.saveToken(response.token);  // Gelen token'ı kaydet
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
  showSubmenu(category: string): void {
    const submenu = this.submenuRef.nativeElement as HTMLElement;

    if (category === 'kadın') {
      submenu.innerHTML = `
        <div>
          <h4>Giyim</h4>
          <ul>
            <li>Elbise</li>
            <li>Tişört</li>
            <li>Gömlek</li>
            <li>Kot Ceket</li>
          </ul>
        </div>
        <div>
          <h4>Ayakkabı</h4>
          <ul>
            <li>Sneaker</li>
            <li>Babet</li>
            <li>Sandalet</li>
          </ul>
        </div>
        <div>
          <h4>Çanta</h4>
          <ul>
            <li>Sırt Çantası</li>
            <li>Bel Çantası</li>
            <li>Omuz Çantası</li>
          </ul>
        </div>
      `;
    } else if (category === 'erkek') {
      submenu.innerHTML = `
        <div>
          <h4>Giyim</h4>
          <ul>
            <li>Tişört</li>
            <li>Mont</li>
            <li>Pantolon</li>
          </ul>
        </div>
        <div>
          <h4>Ayakkabı</h4>
          <ul>
            <li>Sneaker</li>
            <li>Bot</li>
            <li>Loafer</li>
          </ul>
        </div>
      `;
    }
  }
}

