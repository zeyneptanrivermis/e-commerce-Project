import { Component, OnInit } from '@angular/core';
import { SellerProductService } from '../../services/seller-product.service';
import { Product } from '../../../models/product.model';

@Component({
  selector: 'app-seller-products',
  templateUrl: './seller-products.component.html',
  styleUrls: ['./seller-products.component.css'],
  standalone: false
})
export class SellerProductsComponent implements OnInit {
  products: Product[] = [];
  
  constructor(private productService: SellerProductService) {}

  ngOnInit(): void {
    this.loadSellerProducts();
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
    this.productService.getSellerProducts().subscribe({
      next: (data: Product[]) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Ürünler yüklenemedi:', err);
        alert('Ürünler yüklenirken bir hata oluştu. Lütfen tekrar deneyin.');
      }
    });
  }
}
