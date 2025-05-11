import { AuthGuard } from './../../../../core/guards/auth-guard.service';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';

import { MainCategory, Product } from '../../../../models/product.model';
import { PopularityService } from '../../services/popular-products/product-popularity.service';
import { WishlistService } from '../../../user/services/wishlist.service';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-product-item',
  standalone: false,
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.css']  // <-- düzelttik
})
export class ProductItemComponent {

  @Input() product!: Product;
  @Output() add = new EventEmitter<{ product: Product; quantity: number }>();
  @Output() toggleWishlist = new EventEmitter<void>();

  isInWishlist = false;
  categoryLabel = MainCategoryLabel;

  /** Sepete ekleme için seçilen miktar */
  quantity = 1;

  constructor(
    private wishlistService: WishlistService,
    private authService: AuthService,
    private popularity: PopularityService,
    private authGuard: AuthGuard,
    private router: Router
  ) {}

  /** “–” butonu */
  decrease(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  /** “+” butonu */
  increase(): void {
    this.quantity++;
  }

  /** Sepete ekle */
  onAdd(): void {
    this.add.emit({ product: this.product, quantity: this.quantity });
    this.popularity.increment(this.product.id);
  }

  /** Heart ikonuna tıklayınca wishlist toggle */
  onToggleWishlist(): void {
    const user = this.authService.getCurrentUser();
    if (!user?.userId) {
      this.router.navigate(['/login']);
      return;
    }

    this.wishlistService.addToWishlist(this.product.id).subscribe({
      next: () => {
        this.isInWishlist = true;
        this.toggleWishlist.emit();
      },
      error: err => console.error('Wishlist ekleme hatası', err)
    });
  }

  getCategoryKey(value: string): string | null {
      const entry = Object.entries(MainCategory).find(([_, val]) => val === value);
      return entry ? entry[0].toUpperCase() : null; // büyük harfli KEY döner
  }
  normalize(value: string): string {
    return value?.toUpperCase().replace(/\s+/g, '_').replace(/&/g, 'AND');
  }

}

export const MainCategoryLabel: Record<MainCategory, string> = {
  [MainCategory.Clothing]: 'Clothing',
  [MainCategory.Makeup]: 'Makeup',
  [MainCategory.Electronics]: 'Electronics',
  [MainCategory.Pet_Supplies]: 'Pet Supplies',
  [MainCategory.Home_and_Kitchen]: 'Home & Kitchen',
  [MainCategory.Toys_and_Games]: 'Toys & Games',
  [MainCategory.Sports_and_Outdoor]: 'Sports & Outdoor',
  [MainCategory.Hobbies]: 'Hobby Products',
};
