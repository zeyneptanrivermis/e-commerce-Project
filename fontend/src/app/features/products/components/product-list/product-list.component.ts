import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild, PLATFORM_ID, Inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MainCategory, Product, SideCategories } from '../../../../models/product.model';
import { CartService } from '../../../cart/services/cart.service';
import { ProductService } from '../../services/product.service';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-product-list',
  standalone: false,
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit, AfterViewInit, OnDestroy {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  observer?: IntersectionObserver;
  isBrowser: boolean;

  limit = 10;
  skip = 0;
  loading = false;
  allLoaded = false;
  currentCategory: string | null = null;

  @ViewChild('observer', { static: true }) observerElement!: ElementRef;

  constructor(
    private cartService: CartService,
    public route: ActivatedRoute,
    private productService: ProductService,
    private router: Router,
    @Inject(PLATFORM_ID) platformId: Object
  ) {
    this.isBrowser = isPlatformBrowser(platformId);
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.currentCategory = params['category'] || null;
      this.products = [];
      this.filteredProducts = [];
      this.skip = 0;
      this.allLoaded = false;

      if (this.currentCategory) {
        this.productService.getProductsByCategory(this.currentCategory).subscribe(products => {
          this.products = products;
          this.filteredProducts = products;
        });
      } else {
        this.loadProducts();
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.isBrowser && 'IntersectionObserver' in window) {
      this.observer = new IntersectionObserver(entries => {
        const entry = entries[0];
        if (entry.isIntersecting && !this.loading && !this.allLoaded && !this.currentCategory) {
          this.loadProducts();
        }
      }, {
        root: null,
        rootMargin: '0px 0px 200px 0px',
        threshold: 0
      });

      if (this.observerElement?.nativeElement) {
        this.observer.observe(this.observerElement.nativeElement);
      }
    }
  }

  ngOnDestroy(): void {
    if (this.isBrowser && this.observer) {
      this.observer.disconnect();
    }
  }

  trackById(index: number, product: any): number {
    return product?.id ?? index;
  }

  private loadProducts(): void {
    if (this.loading || this.allLoaded) return;

    this.loading = true;

    this.productService.getProducts(this.limit, this.skip).subscribe({
      next: data => {
        if (data?.length) {
          this.products = [...this.products, ...data];
          this.filteredProducts = this.products;
          this.skip += this.limit;

          if (data.length < this.limit) {
            this.allLoaded = true;
            if (this.isBrowser && this.observer) {
              this.observer.disconnect();
            }
          }
        } else {
          this.allLoaded = true;
          if (this.isBrowser && this.observer) {
            this.observer.disconnect();
          }
        }
        this.loading = false;
      },
      error: err => {
        console.error('🔴 Ürünler alınamadı:', err);
        this.loading = false;
        this.allLoaded = true;
        if (this.isBrowser && this.observer) {
          this.observer.disconnect();
        }
      }
    });
  }

  filterByCategory(category: string | 'All') {
    this.router.navigate([], {
      queryParams: { category: category === 'All' ? null : category },
      queryParamsHandling: 'merge'
    });
  }

  clearCategory() {
    this.router.navigate([], {
      queryParams: { category: null },
      queryParamsHandling: 'merge'
    });
  }

  getSelectedSideCategories(category: MainCategory, indexes: number[]): string[] {
    const all = SideCategories[category] || [];
    return indexes.map(i => all[i]).filter(Boolean);
  }

  onAddToCart(event: { product: Product; quantity: number }): void {
    const { product, quantity } = event;
    this.cartService.addToCart(product.id, quantity).subscribe({
      next: () => console.log(`🛒 ${product.name} sepete eklendi (x${quantity}).`),
      error: err => console.error('❌ Sepete eklenirken hata oluştu:', err)
    });
  }
}

function normalize(value: string): string {
  return value?.toUpperCase().replace(/[\s&]/g, '_');
}
