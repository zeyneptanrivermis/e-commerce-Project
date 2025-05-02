import { AuthService } from './../../auth/services/auth.service';
import { User } from './../../../models/user.model';
import { Component, OnInit } from '@angular/core';
import { WishlistService } from '../services/wishlist.service';
import { Product } from '../../../models/product.model';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css'],
  standalone:false
})

export class WishlistComponent implements OnInit {

  currentUser: User | null= null;
  wishlist: Product[] = [];

  constructor(private authService :AuthService, private wishlistService: WishlistService, 
    private router: Router, private tokenService: TokenService) {}

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
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
    this.wishlistService.removeFromWishlist(productId).subscribe({
      next: () => this.loadWishlist(),
      error: (err) => console.error('Hata oluştu:', err)
    });
  }
  
  
  redirect(){
    this.router.navigate(["/products"]);
  }
}
