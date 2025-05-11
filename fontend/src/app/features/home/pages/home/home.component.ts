import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../products/services/product.service';
import { Product } from '../../../../models/product.model';
import { MainCategory } from '../../../../models/product.model';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  standalone:false
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  categories = [
    {
      id: MainCategory.Electronics,
      name: 'Electronics',
      image: 'assets/images/categories/electronics.jpg'
    },
    {
      id: MainCategory.Clothing,
      name: 'Clothing',
      image: 'assets/images/canta.png'
    },
    {
      id: MainCategory.Hobbies,
      name: 'Hobbies',
      image: 'assets/images/categories/hobbies.jpg'
    },
    {
      id: MainCategory.Home_and_Kitchen,
      name: 'Home and Kitchen',
      image: 'assets/images/categories/home.jpg'
    }
  ];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    // İlk 20 ürünü al (limit=20, skip=0)
    this.productService.getProducts(20, 0).subscribe({
      next: (res) => {
        // Ürünleri rastgele karıştır
        this.products = this.shuffleArray(res);
      },
      error: (err) => console.error('Ürünler yüklenemedi', err)
    });
  }

  // Fisher-Yates shuffle algoritması
  private shuffleArray(array: Product[]): Product[] {
    const newArray = [...array];
    for (let i = newArray.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [newArray[i], newArray[j]] = [newArray[j], newArray[i]];
    }
    return newArray;
  }

  normalize(name: string): string {
    return name.toUpperCase().replace(/\s+/g, '_').replace(/&/g, 'AND');
  }

}
