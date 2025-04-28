import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/services/auth.service';
import { UserService } from '../services/user.service';
import { WishlistService } from '../services/wishlist.service';
import { Product } from '../../../models/product.model';
import { User } from '../../../models/user.model';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  standalone: false
})
export class UserProfileComponent implements OnInit {

  currentUser: User | null = null;
  wishlist: Product[] = [];

  editEmail: boolean = false;
  editBirthday: boolean = false;
  editName: boolean = false;
  editSurname: boolean = false;

  profileForm!: FormGroup;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private wishlistService: WishlistService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadUserInfo();
    this.loadWishlist();
    this.initializeForm();
  }

  initializeForm(): void {
    this.profileForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      birthday: ['', [this.noFutureDateValidator]],
      name: ['', [Validators.required, Validators.maxLength(50)]],
      surname: ['', [Validators.required, Validators.maxLength(50)]]
    });
  }

  noFutureDateValidator(control: AbstractControl): ValidationErrors | null {
    const selectedDate = new Date(control.value);
    const today = new Date();
    if (selectedDate > today) {
      return { futureDate: true };
    }
    return null;
  }

  loadUserInfo(): void {
    this.userService.getUserInfo().subscribe({
      next: (userInfo: User) => {
        this.currentUser = userInfo;
        this.profileForm.patchValue(userInfo);  // Gelen veriyi forma bastırıyoruz
        console.log('User Info:', userInfo);
      },
      error: (error) => {
        console.error('Error fetching user info:', error);
      }
    });
  }

  loadWishlist(): void {
    const user = this.authService.getCurrentUser();
    if (user && user.wishListId) {
      this.wishlistService.getWishlist(user.wishListId).subscribe({
        next: (data) => {
          this.wishlist = data;
        },
        error: (error) => {
          console.error('Error loading wishlist', error);
        }
      });
    }
  }

  toggleEdit(field: string): void {
    if (!this.currentUser) return;

    switch (field) {
      case 'email':
        this.editEmail = !this.editEmail;
        if (!this.editEmail && this.profileForm.get('email')?.valid) {
          this.currentUser.email = this.profileForm.get('email')?.value;
          this.saveProfileChanges();
        }
        break;
      case 'birthday':
        this.editBirthday = !this.editBirthday;
        if (!this.editBirthday && this.profileForm.get('birthday')?.valid) {
          this.currentUser.birthday = this.profileForm.get('birthday')?.value;
          this.saveProfileChanges();
        }
        break;
      case 'name':
        this.editName = !this.editName;
        if (!this.editName && this.profileForm.get('name')?.valid) {
          this.currentUser.name = this.profileForm.get('name')?.value;
          this.saveProfileChanges();
        }
        break;
      case 'surname':
        this.editSurname = !this.editSurname;
        if (!this.editSurname && this.profileForm.get('surname')?.valid) {
          this.currentUser.surname = this.profileForm.get('surname')?.value;
          this.saveProfileChanges();
        }
        break;
    }
  }

  saveProfileChanges(): void {
    if (!this.currentUser) {
      return;
    }

    this.userService.updateUser(this.currentUser).subscribe({
      next: (response) => {
        console.log('Kullanıcı başarıyla güncellendi', response);
        // Başarı bildirimi eklenebilir
      },
      error: (error) => {
        console.error('Kullanıcı güncelleme hatası', error);
      }
    });
  }

  addToWishlist(product: Product): void {
    if (this.currentUser) {
      this.wishlistService.addToWishlist(this.currentUser.id, product.id).subscribe({
        next: () => {
          this.loadWishlist();
        },
        error: (error) => {
          console.error('Error adding to wishlist', error);
        }
      });
    }
  }

  removeFromWishlist(product: Product): void {
    if (this.currentUser) {
      this.wishlistService.removeFromWishlist(this.currentUser.id, product.id).subscribe({
        next: () => {
          this.loadWishlist();
        },
        error: (error) => {
          console.error('Error removing from wishlist', error);
        }
      });
    }
  }

  redirect(): void {
    this.router.navigate(['/products']);
  }
}
