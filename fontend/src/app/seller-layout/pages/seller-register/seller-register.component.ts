import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SellerAuthService } from '../../services/seller-auth.service';

@Component({
  selector: 'app-seller-register',
  templateUrl: './seller-register.component.html',
  styleUrls: ['./seller-register.component.css'],
  standalone: false
})
export class SellerRegisterComponent {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: SellerAuthService
  ) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      surname: ['', Validators.required],
      shopName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      console.warn('Form geçersiz:', this.registerForm.value);
      return;
    }

    this.authService.register(this.registerForm.value).subscribe({
      next: (res) => console.log('✅ Kayıt başarılı', res),
      error: (err) => console.error('🔴 Kayıt hatası:', err)
    });
  }
}
