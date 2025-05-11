import { AuthService } from './../../auth/services/auth.service';
import { User } from './../../../models/user.model';
import { Component, OnInit } from '@angular/core';
import { WishlistService } from '../services/wishlist.service';
import { Product } from '../../../models/product.model';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenService } from '../../../core/services/token.service';
import { CartService } from '../../cart/services/cart.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css'],
  standalone:false
})

export class WishlistComponent implements OnInit {


  currentUser: User | null = null;
  wishlist: Product[] = [];

  quantities: { [productId: number]: number } = {};

  constructor(
    private authService: AuthService,
    private wishlistService: WishlistService,
    private cartService: CartService,                           // ← inject eklendi
    private router: Router,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser() || null;
    this.loadWishlist();
  }

  loadWishlist(): void {
    this.wishlistService.getWishlist().subscribe({
      next: products => {
        this.wishlist = products;
        this.wishlist.forEach(p => {
          if (!this.quantities[p.id]) {
            this.quantities[p.id] = 1;
          }
        });
      },
      error: err => console.error('Wishlist yüklenirken hata oluştu', err)
    });
  }

  decreaseQuantity(product: Product): void {
    const q = this.quantities[product.id];
    if (q > 1) {
      this.quantities[product.id] = q - 1;
    }
  }

  increaseQuantity(product: Product): void {
    this.quantities[product.id] = this.quantities[product.id] + 1;
  }

  /** İKİ ARGÜMAN gönderiyoruz: product.id ve miktar */
  addToCart(product: Product): void {
    const qty = this.quantities[product.id];
    this.cartService.addToCart(product.id, qty).subscribe({
      next: () => {
        console.log(`Sepete eklendi: ${product.name} (x${qty})`);
      },
      error: err => console.error('Sepete eklenirken hata oluştu:', err)
    });
  }

  removeFromWishlist(productId: number): void {
    this.wishlistService.removeFromWishlist(productId).subscribe({
      next: () => this.loadWishlist(),
      error: err => console.error('Hata oluştu:', err)
    });
  }

  redirect(): void {
    this.router.navigate(['/products']);
  }
}
