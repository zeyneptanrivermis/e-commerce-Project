import { AuthService } from './../../auth/services/auth.service';
import { User } from './../../../models/user.model';
import { Component, OnInit } from '@angular/core';
import { WishlistService } from '../services/wishlist.service';
import { Product } from '../../../models/product.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css'],
  standalone:false
})
export class WishlistComponent implements OnInit {

  currentUser: User | null= null;
  wishlist: Product[] = [];

  constructor(private authService :AuthService, private wishlistService: WishlistService, private router: Router) {}

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    const user = this.authService.getCurrentUser();
    this.wishlistService.getWishlist().subscribe({
      next: (products) => {
        this.wishlist = products;
      },
      error: (err) => {
        console.error('Wishlist yüklenirken hata oluştu', err);
      }
    });
  }

  removeFromWishlist(productId: number): void {
    const user = this.authService.getCurrentUser();
    this.wishlistService.removeFromWishlist(user!.userId, productId).subscribe({
      next: () => {
        this.wishlist = this.wishlist.filter(p => p.id !== productId);
      },
      error: (err) => {
        console.error('Ürün silinirken hata oluştu', err);
      }
    });
  }

  redirect(){
    this.router.navigate(["/products"]);
  }
}
