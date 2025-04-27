import { Product } from './../../../models/product.model';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../auth/services/auth.service';
import { UserService } from '../services/user.service';
import { WishlistService } from '../services/wishlist.service';
import { User } from '../../../models/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
  standalone: false
})
export class UserProfileComponent implements OnInit {

  currentUser: User | null= null;
  wishlist: Product[] = [];

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private wishlistService: WishlistService,
    private router: Router)
   { }

  ngOnInit(): void {
    this.loadUserInfo();
    this.loadWishlist();
  }

  // Kullanıcı bilgilerini yükleme
  loadUserInfo(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.currentUser = user;
    }
  }

  // Kullanıcının wishlist'ini yükleme
  loadWishlist(): void {
    const user = this.authService.getCurrentUser();
    if (user) {
      this.wishlistService.getWishlist(user.wishListId).subscribe(
        (data) => {
          this.wishlist = data;
        },
        (error) => {
          console.error('Error loading wishlist', error);
        }
      );
    }
  }

  // Wishlist'e ürün ekleme
  addToWishlist(product: Product): void {
    if (this.currentUser) {
      this.wishlistService.addToWishlist(this.currentUser.wishListId, product).subscribe(
        (response) => {
          this.loadWishlist();  // Yeniden wishlist'i yükle
        },
        (error) => {
          console.error('Error adding to wishlist', error);
        }
      );
    }
  }

  // Wishlist'ten ürün silme
  removeFromWishlist(product: Product): void {
    if (this.currentUser) {
      this.wishlistService.removeFromWishlist(this.currentUser.wishListId, product).subscribe(
        (response) => {
          this.loadWishlist();  // Yeniden wishlist'i yükle
        },
        (error) => {
          console.error('Error removing from wishlist', error);
        }
      );
    }
  }
  redirect(){
    this.router.navigate(["/products"]);
  }

}
