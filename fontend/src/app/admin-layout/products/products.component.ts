import { Component } from '@angular/core';
import { AdminApiService } from '../service/admin-api.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-products',
  standalone: false,
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent {
  products: Product[] = [];

  constructor(private api: AdminApiService) {}

  ngOnInit() {
    this.api.getAllProducts().subscribe(p => {
      console.log('📦 Gelen ürünler:', p);  // ← Bunu kontrol et
      this.products = p;
    });
  }

  loadProducts() {
    this.api.getAllProducts().subscribe(data => {
      this.products = data;
    });
  }

  cancelProduct(id: number) {
    this.api.cancelProduct(id).subscribe(() => this.loadProducts());
  }

  deleteProduct(id: number) {
    if (confirm('Bu ürünü kalıcı olarak silmek istediğinize emin misiniz?')) {
      this.api.deleteProduct(id).subscribe(() => this.loadProducts());
    }
  }
  
}
