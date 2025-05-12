import { Component, OnInit } from '@angular/core';
import { AdminApiService } from '../service/admin-api.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-products',
  standalone: false,
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
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
    const product = this.products.find(p => p.id === id);
    if (product) {
      if (product.cancelled) {
        this.api.uncancelProduct(id).subscribe(() => {
          product.cancelled = false;
        });
      } else {
        this.api.cancelProduct(id).subscribe(() => {
          product.cancelled = true;
        });
      }
    }
  }

}
