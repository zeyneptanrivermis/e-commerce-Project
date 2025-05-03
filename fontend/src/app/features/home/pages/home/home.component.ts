import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../products/services/product.service';
import { Product } from '../../../../models/product.model'; // modelin varsa
import { PopularityService } from '../../../products/services/popular-products/product-popularity.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone:false
})
export class HomeComponent implements OnInit {
  products: Product[] = [];

  constructor(private productService: PopularityService) {}

  ngOnInit(): void {
    this.productService.getPopularProducts().subscribe({
      next: (res) => this.products = res,
      error: (err) => console.error('Popüler ürünler yüklenemedi', err)
    });
    
  }
}
