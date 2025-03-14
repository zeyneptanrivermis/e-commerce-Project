import { MainCategory, SideCategories } from './../../models/product.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Component } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-list',
  standalone: false,
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent {
  products: Product[] = [
    { id: 1, name: 'T-shirt', description: 'Description A', price: 50, mainCategory:  MainCategory.Clothing, sideCategories: SideCategories[MainCategory.Clothing].slice(0, 2) },
    { id: 2, name: 'Phone X', description: 'Description B', price: 3000, mainCategory: MainCategory.Electronics, sideCategories: this.getSelectedSideCategories(MainCategory.Electronics, [0, 4])},
    { id: 3, name: 'Lipstick', description: 'Description C', price: 20, mainCategory: MainCategory.Makeup, sideCategories: SideCategories[MainCategory.Makeup].slice(2,3)}
  ];

  filteredProducts: Product[] = this.products;

  constructor(
    private cartService: CartService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const category = params['category'];
      if (category) {
        this.filterByCategory(category);
      } else {
        this.filteredProducts = this.products; // Show all products if no category selected
      }
    });
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product);
  }

filterByCategory(category: MainCategory | keyof typeof SideCategories | 'All') {
  if (category === 'All') {
    this.filteredProducts = this.products; // Show all products
  } else {
    this.filteredProducts = this.products.filter(product =>
      product.mainCategory === category || product.sideCategories?.includes(category)
    );
  }
}

  getSelectedSideCategories(category: MainCategory, indexes: number[]): string[] {
    const allCategories = SideCategories[category] || [];
    return indexes
      .map(i => allCategories[i]) 
      .filter(Boolean); 
  }
}
