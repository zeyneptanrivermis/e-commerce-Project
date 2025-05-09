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
    private sellerAuthService: SellerAuthService
  ) {}

  ngOnInit(): void {
    
  }

}
