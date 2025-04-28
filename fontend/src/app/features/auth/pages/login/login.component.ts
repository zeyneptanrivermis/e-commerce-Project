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
  
    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;
  
    this.authService.login(email, password).subscribe({
      next: (response) => {
        console.log('Login response:', response);  // <-- Burası önemli
  
        if (response && response.token) {
          this.authService.saveToken(response.token);  // <-- Sadece "token" kaydediyoruz
          console.log('Token başarıyla kaydedildi.');
          this.router.navigate(['/']);
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
