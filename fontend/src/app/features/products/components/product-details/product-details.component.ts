import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../../../models/product.model';
import { CartService } from '../../../cart/services/cart.service';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
  standalone:false
})
export class ProductDetailsComponent implements OnInit {
  product!: Product;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.productService.getProductById(+productId).subscribe({
        next: (data) => (this.product = data),
        error: (err) => console.error('Ürün alınamadı:', err)
      });
    }
  }

  addToCart(productId: number): void {
    this.cartService.addToCart(productId, 1).subscribe({
      next: () => {
        alert('Ürün sepete eklendi ✅');
      },
      error: (err) => {
        console.error('❌ Sepete eklenemedi:', err);
        alert('Ürün sepete eklenirken bir hata oluştu!');
      }
    });
  }
}
