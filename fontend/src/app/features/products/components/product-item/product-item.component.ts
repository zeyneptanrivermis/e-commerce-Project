import { AuthGuard } from './../../../../core/guards/auth-guard.service';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../../../models/product.model';
import { PopularityService } from '../../services/popular-products/product-popularity.service';
import { WishlistService } from '../../../user/services/wishlist.service';
import { AuthService } from '../../../auth/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-product-item',
  standalone: false,
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.css'
})
export class ProductItemComponent {

  @Input() product!: Product;
  @Output() add = new EventEmitter<Product>();
  private productPopularity: number= 0;
  isInWishlist = false;

  constructor(  private wishlistService: WishlistService,private authService: AuthService,
                private popularity: PopularityService, private authguard: AuthGuard, private router: Router){}
  
  onAdd() {
    this.add.emit(this.product);
    this.popularity.increment(this.productPopularity);
  }

  onToggleWishlist(): void {
    const user = this.authService.getCurrentUser();
  
    if (!user || !user.userId) {
      console.warn("Not logged in");
      this.router.navigate(['/login']);
      return;
    }
  
    this.wishlistService.addToWishlist(this.product.id).subscribe({
      next: () => {
        this.isInWishlist = true;
      },
      error: (err) => console.error("Wishlist ekleme hatası", err)
    });
  }
  
}
