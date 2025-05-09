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
    this.productService.getSellerProducts().subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Ürünler yüklenemedi:', err)
    });
  }
}
