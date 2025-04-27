import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(30)]],
      surname: ['', [Validators.required, Validators.maxLength(30)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(10)]],
      confirmPassword: ['', [Validators.required]],
      birthday: ['', [Validators.required]],
      gender: [''],
      acceptOffers: [false],
      acceptEmails: [false],
      acceptTerms: [false, [Validators.requiredTrue]]
    });
  }


  selectGender(gender: string) {
    this.registerForm.patchValue({ gender });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }

  goToRegister(): void {
    this.router.navigate(['/register']);
  }

  doRegister(): void {
    console.log('✅ Register butonuna basıldı. Fonksiyon çalıştı.');

    const { password, confirmPassword } = this.registerForm.value;

    if (password !== confirmPassword) {
      this.errorMessage = 'Şifreler uyuşmuyor!';
      return;
    }

    if (this.registerForm.invalid) {
      this.errorMessage = 'Lütfen formu eksiksiz doldurun!';
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: (response) => {
        alert('Kayıt başarılı!');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Kayıt hatası', error);
        this.errorMessage = 'Bu e-posta zaten kullanılıyor veya kayıt sırasında hata oluştu!';
      }
    });
  }

}
