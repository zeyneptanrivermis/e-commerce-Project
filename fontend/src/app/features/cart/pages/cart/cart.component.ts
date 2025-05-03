import { Component, Inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CartService, CartItem } from '../../services/cart.service';
import { isPlatformBrowser } from '@angular/common';
import { PLATFORM_ID } from '@angular/core';

@Component({
  selector: 'app-cart',
  standalone: false,
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  isBrowser: boolean;

  constructor(
    private cartService: CartService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    // SSR kontrolü
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  ngOnInit(): void {
    if (!this.isBrowser) return;
    this.loadCart();
  }

  private isTokenValid(token: string | null): boolean {
    if (!token) return false;
    if (token === 'undefined' || token.trim() === '') return false;
    if (!token.includes('.') || token.split('.').length !== 3) return false;
    return true;
  }

  loadCart(): void {
    console.log(this.cartItems)
    this.cartService.listCartItems().subscribe({
      next: (items) => {
        console.log('Sepet öğeleri:', items); // Gelen yanıtı gör
        this.cartItems = items;
      },
      error: (err) => {
        console.error('Sepet yüklenemedi:', err); // Hata detayları
      }
    });

    this.cartService.getCartTotal().subscribe({
      next: (total) => {
        console.log('Toplam tutar:', total); // Gelen toplam tutar yanıtını gör
        this.totalPrice = total;
      },
      error: (err) => {
        console.error('Toplam tutar yüklenemedi:', err); // Hata detayları
      }
    });
  }


  removeItem(item: CartItem): void {
    const productId = item.product?.productId ?? null;
    console.log('Silinecek ürün ID\'si:', productId);

    if (productId !== undefined) {
      this.cartService.removeFromCart(productId).subscribe({
        next: () => this.loadCart(),
        error: (err) => console.error('Ürün silinemedi:', err)
      });
    } else {
      console.error('Ürün ID bulunamadı, item:', item);
    }
  }




  increaseQuantity(item: CartItem): void {
    const newQty = item.quantity + 1;
    this.cartService.updateQuantity(item.product.productId, newQty).subscribe({
      next: () => this.loadCart(),
      error: (err) => console.error('Miktar artırılamadı:', err)
    });
  }


  decreaseQuantity(item: CartItem): void {
    const newQty = item.quantity - 1;
    if (newQty >= 1) {
      this.cartService.updateQuantity(item.product.productId, newQty).subscribe({
        next: () => this.loadCart(),
        error: (err) => console.error('Miktar azaltılamadı:', err)
      });
    }
  }


  goToProducts(): void {
    this.router.navigate(['/products']);
  }

  checkout(): void {
    this.router.navigate(['/checkout']);
  }
}
