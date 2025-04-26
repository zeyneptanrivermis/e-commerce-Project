import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { MainCategory, Product, SideCategories } from '../../../../models/product.model';
import { CartService } from '../../../cart/services/cart.service';
import { ProductService } from '../../services/product.service';



@Component({
  selector: 'app-product-list',
  standalone: false,
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})

export class ProductListComponent implements OnInit, AfterViewInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  observer!: IntersectionObserver; //asagi kaydirdikce yukleme icin, kaydirma denetleyici

  limit = 10; //sona gelindiginde kac yeni urun yuklenecegi
  skip = 0;
  loading = false; //yukleniyor mu?
  allLoaded = false; // yukleyecek urun yoksa true

  @ViewChild('observer', { static: true }) observerElement!: ElementRef;

  constructor(
    private cartService: CartService,
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit() {
    const isPopularRoute = this.route.routeConfig?.path === 'popular';
  
    if (isPopularRoute) {
      this.loadPopularProducts(); // özel servis
    } else {
      this.loadProducts(); // scroll tabanlı yükleme
    }
  
    // Query param ile kategori filtreleme
    this.route.queryParams.subscribe(params => {
      const category = params['category'];
      if (category) {
        this.filterByCategory(category);
      } else {
        this.filteredProducts = this.products;
      }
    });
  }

  ngAfterViewInit(): void {
    if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
      this.observer = new IntersectionObserver(
        entries => {
          const entry = entries[0];
          if (entry.isIntersecting && !this.loading) {
            console.log('✅ Scroll tetiklendi, ürün yükleniyor...');
            this.loadProducts();
          }
        },
        {
          root: null,
          rootMargin: '0px 0px 200px 0px', // Alt kenar gözlemlemesi için marj artırıldı
          threshold: 0
        }
      );

      // DOM stabilize olduktan sonra bağla
      setTimeout(() => {
        if (this.observerElement?.nativeElement) {
          this.observer.observe(this.observerElement.nativeElement);
        }
      }, 200); // DOM tam oturması için gecikme eklendi
    }
  }

  loadProducts(): void {
    if (this.loading || this.allLoaded) return;

    this.loading = true;
    this.productService.getProducts(this.limit, this.skip).subscribe(data => {
      if (data && data.length > 0) {
        this.products = [...this.products, ...data];
        this.filteredProducts = [...this.products];
        this.skip += this.limit;

        if (data.length < this.limit) {
          this.allLoaded = true;
          this.observer?.disconnect();
        }
      } else {
        this.allLoaded = true;
        this.observer?.disconnect();
      }

      this.loading = false;
    }, error => {
      this.loading = false;
      this.allLoaded = true;
      this.observer?.disconnect();
      console.error('🔴 Ürünler alınamadı:', error);
    });
  }

  addToCart(product: Product) {
    this.cartService.addToCart(product.id, 1).subscribe(() => {
    });
  }

  filterByCategory(category: MainCategory | keyof typeof SideCategories | 'All') {
    if (category === 'All') {
      this.filteredProducts = this.products;
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
  
  loadPopularProducts(): void {
    this.loading = true;
    this.productService.getPopularProducts().subscribe(data => {
      this.products = data;
      this.filteredProducts = data;
      this.allLoaded = true; // scroll'a gerek yok
      this.loading = false;
      this.observer?.disconnect();
    }, error => {
      console.error('🔴 Popüler ürünler alınamadı:', error);
      this.loading = false;
      this.allLoaded = true;
    });
  }

}