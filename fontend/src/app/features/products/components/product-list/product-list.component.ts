import { ActivatedRoute } from '@angular/router';
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
  observer!: IntersectionObserver;

  limit = 10;
  skip = 0;
  loading = false;
  allLoaded = false;

  @ViewChild('observer', { static: true }) observerElement!: ElementRef;

  constructor(
    private cartService: CartService,
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit() {
    const isPopularRoute = this.route.routeConfig?.path === 'popular';

    if (isPopularRoute) {
      this.loadPopularProducts();
    } else {
      this.loadProducts();
    }

    this.route.queryParams.subscribe(params => {
      const category = params['category'];
      if (category) {
        this.filterByCategory(category);
      } else {
        this.filteredProducts = [...this.products];
      }
    });
  }

  ngAfterViewInit(): void {
    if (typeof window !== 'undefined' && 'IntersectionObserver' in window) {
      this.observer = new IntersectionObserver(
        entries => {
          const entry = entries[0];
          if (entry.isIntersecting && !this.loading && !this.allLoaded) {
            this.loadProducts();
          }
        },
        {
          root: null,
          rootMargin: '0px 0px 200px 0px',
          threshold: 0
        }
      );

      // DOM’a bağlama işlemi eksikti
      if (this.observerElement?.nativeElement) {
        this.observer.observe(this.observerElement.nativeElement);
      }
    }
  }

  trackById(index: number, product: any): number {
    return product?.id ?? index;
  }

 loadProducts(): void {
  if (this.loading || this.allLoaded) return;

  this.loading = true;

  this.productService.getProducts(this.limit, this.skip).subscribe(data => {
    if (data && data.length > 0) {
      // DÖNÜŞÜM YAPMA, olduğu gibi bırak
      this.products = [...this.products, ...data];
      this.skip += this.limit;
      this.applyFilter();

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
    console.error('🔴 Ürünler alınamadı:', error);
    this.loading = false;
    this.allLoaded = true;
    this.observer?.disconnect();
  });
}

  loadPopularProducts(): void {
    this.loading = true;
    this.productService.getProducts(this.limit, this.skip).subscribe({
      next: (data) => {
        this.products = [...this.products, ...data];
        this.skip += this.limit;
        this.applyFilter();
    
        if (data.length < this.limit) {
          this.allLoaded = true;
          this.observer?.disconnect();
        }
    
        this.loading = false;
      },
      error: (err) => {
        console.error('🔴 Ürünler alınamadı:', err);
        this.loading = false;
        this.allLoaded = true;
        this.observer?.disconnect();
      }
    });
  }

  filterByCategory(category: string | 'All') {
    if (category === 'All') {
      this.filteredProducts = [...this.products];
      return;
    }
  
    this.filteredProducts = this.products.filter(product =>
      product.mainCategory === category || product.sideCategories?.includes(category)
    );
  }
  

  applyFilter() {
    const category = this.route.snapshot.queryParams['category'];
    if (category && category !== 'All') {
      const matchedEnum = Object.values(MainCategory).find(val => val === category);
      this.filteredProducts = this.products.filter(product =>
        product.mainCategory === matchedEnum || product.sideCategories?.includes(category)
      );
    } else {
      this.filteredProducts = [...this.products];
    }
  }
  

  getSelectedSideCategories(category: MainCategory, indexes: number[]): string[] {
    const allCategories = SideCategories[category] || [];
    return indexes.map(i => allCategories[i]).filter(Boolean);
  }

  addToCart(product: Product): void {
    const quantity = 1; // Varsayılan olarak 1 adet eklensin
  
    this.cartService.addToCart(product.id, quantity).subscribe({
      next: () => {
        console.log(`🛒 ${product.name} sepete eklendi.`);
        // İstersen bir toast gösterimi veya animasyon tetikleyebilirsin
      },
      error: (err) => {
        console.error('❌ Sepete eklenirken hata oluştu:', err);
        // Eğer kullanıcı login değilse yönlendirme gibi şeyler burada yapılabilir
      }
    });
  }
  
}


