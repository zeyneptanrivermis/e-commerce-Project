import { ProductService } from './../../../features/products/services/product.service';
import { Component, OnInit } from '@angular/core';
import { SellerProductService } from '../../services/seller-product.service';
import { Product, Seller } from '../../../models/product.model';
import { SellerAuthService } from '../../services/seller-auth.service';

@Component({
  selector: 'app-seller-products',
  templateUrl: './seller-products.component.html',
  styleUrls: ['./seller-products.component.css'],
  standalone: false
})
export class SellerProductsComponent implements OnInit {
  products: Product[] = [];
  currentSeller: Seller | null = null;
  
  constructor(private productService: SellerProductService,
    private sellerAuthService: SellerAuthService,
  ) {}

  ngOnInit(): void {
    
  }

  onDelete(productId: number): void {
    if (confirm("Bu ürünü silmek istediğinize emin misiniz?")) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          this.loadSellerProducts(); // Refresh list
        },
        error: (err) => {
          console.error("Silme hatası:", err);
          alert("Ürün silinemedi. Lütfen tekrar deneyin.");
        }
      });
    }
  }
loadSellerProducts(): void {
    const sellerId = Number(localStorage.getItem('userId')); // veya token içinden al
    if (!sellerId) {
      console.error('Satıcı ID bulunamadı');
      return;
    }

    this.productService.getSellerProducts(sellerId).subscribe({
      next: (data: Product[]) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Ürünler yüklenemedi:', err);
      }
    });
  }
}
