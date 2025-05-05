import { Component } from '@angular/core';
import { AdminApiService, Product } from '../service/admin-api.service';

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
}
