import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductService } from '../../../products/services/product.service';
import { Product } from '../../../../models/product.model';
import { MainCategory } from '../../../../models/product.model';
import { CATEGORY_IMAGES } from '../../CategoryImages';

interface Slide {
  image: string;
  title: string;
  description: string;
  buttonText: string;
  link: string;
}

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss', './home.component.css']
})
export class HomeComponent implements OnInit {
  currentSlide = 0;
  slides: Slide[] = [
    {
      image: 'assets/images/araba.png',
      title: 'New Season Products',
      description: 'Discover our latest collection',
      buttonText: 'Shop Now',
      link: '/products'
    },
    {
      image: 'assets/images/kolye.png',
      title: 'Special Discounts',
      description: 'Up to 50% off on selected items',
      buttonText: 'Grab the Deals',
      link: '/products'
    },
    {
      image: 'assets/images/hediye.png',
      title: 'New Arrivals',
      description: 'Check out our newest products',
      buttonText: 'Explore',
      link: '/products'
    }
  ];

  categories = [
    {
      name: 'Electronics',
      image: 'assets/images/elektronik.png'
    },
    {
      name: 'Clothing',
      image: 'assets/images/canta.png'
    },
    {
      name: 'Home & Kitchen',
      image: 'assets/images/mutfak.png'
    },
    {
      name: 'Makeup',
      image: 'assets/images/makeup.jpg'
    }
  ];

  products: Product[] = [
    {
      id: 1,
      name: 'Product 1',
      description: 'Description of product 1',
      price: 199.99,
      image: 'assets/images/products/product1.jpg',
      seller: {
        id: 1,
        name: 'Seller 1',
        email: 'seller1@example.com'
      },
      reviews: [],
      cancelled: false
    },
    {
      id: 2,
      name: 'Product 2',
      description: 'Description of product 2',
      price: 299.99,
      image: 'assets/images/products/product2.jpg',
      seller: {
        id: 2,
        name: 'Seller 2',
        email: 'seller2@example.com'
      },
      reviews: [],
      cancelled: false
    },
    {
      id: 3,
      name: 'Product 3',
      description: 'Description of product 3',
      price: 399.99,
      image: 'assets/images/products/product3.jpg',
      seller: {
        id: 3,
        name: 'Seller 3',
        email: 'seller3@example.com'
      },
      reviews: [],
      cancelled: false
    },
    {
      id: 4,
      name: 'Product 4',
      description: 'Description of product 4',
      price: 499.99,
      image: 'assets/images/products/product4.jpg',
      seller: {
        id: 4,
        name: 'Seller 4',
        email: 'seller4@example.com'
      },
      reviews: [],
      cancelled: false
    },
    {
      id: 5,
      name: 'Product 5',
      description: 'Description of product 5',
      price: 50.99,
      image: 'assets/images/products/product5.jpg',
      seller: {
        id: 5,
        name: 'Seller 5',
        email: 'seller5@example.com'
      },
      reviews: [],
      cancelled: false
    }
  ];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.startCarousel();
    this.productService.getProducts(28, 0).subscribe({
      next: (res) => {
        this.products = this.shuffleArray(res);
      },
      error: (err) => console.error('Failed to load products', err)
    });
  }

  startCarousel(): void {
    setInterval(() => {
      this.nextSlide();
    }, 5000); // Auto slide every 5 seconds
  }

  nextSlide(): void {
    this.currentSlide = (this.currentSlide + 1) % this.slides.length;
  }

  prevSlide(): void {
    this.currentSlide = (this.currentSlide - 1 + this.slides.length) % this.slides.length;
  }

  goToSlide(index: number): void {
    this.currentSlide = index;
  }

  private shuffleArray<T>(array: T[], limit?: number): T[] {
    const newArray = [...array];
    const n = newArray.length;

    for (let i = n - 1; i > 0; i--) {
      const randomBuffer = new Uint32Array(1);
      crypto.getRandomValues(randomBuffer); // Secure random
      const j = randomBuffer[0] % (i + 1);
      [newArray[i], newArray[j]] = [newArray[j], newArray[i]];
    }

    return limit ? newArray.slice(0, limit) : newArray;
  }


  normalize(name: string): string {
    return name.toUpperCase().replace(/\s+/g, '_').replace(/&/g, 'AND');
  }

  normalizeCategory(category: string): string {
    return category.toUpperCase().replace(/\s+/g, '_').replace(/&/g, 'AND');
  }
    getCategoryImage(category?: string | null): string {
      if (!category) return CATEGORY_IMAGES['DEFAULT'];

      const key = this.normalizeCategory(category);
      return CATEGORY_IMAGES[key] || CATEGORY_IMAGES['DEFAULT'];
    }

}
