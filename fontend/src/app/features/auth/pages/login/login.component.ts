import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  doLogin(): void {
    if (this.loginForm.invalid) {
      this.errorMessage = 'Lütfen geçerli bir e-posta ve şifre girin.';
      return;
    }

    const { email, password } = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (response) => {
        if (response && response.token) {
          this.authService.saveToken(response.token);

          const roles = this.authService.getUserRoles();
          console.log('🎫 Kullanıcı rolleri:', roles);

          // ✅ ROL BAZLI YÖNLENDİRME
          if (roles.includes('ROLE_ADMIN')) {
            this.router.navigate(['/admin']);
          } else if (roles.includes('ROLE_SELLER')) {
            this.router.navigate(['/seller/dashboard']);
          } else if (roles.includes('ROLE_CUSTOMER')) {
            this.router.navigate(['/']);
          } else {
            this.router.navigate(['/']); // varsayılan
          }
        } else {
          this.errorMessage = 'Sunucudan geçerli bir token alınamadı.';
        }
      },
      error: (error) => {
        console.error('Login error:', error);
        this.errorMessage = 'Giriş başarısız. Lütfen bilgilerinizi kontrol edin.';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  goToRegister(): void {
    this.router.navigate(['/register']);
  }
}
