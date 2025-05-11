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

<<<<<<< HEAD
  ngOnInit(): void {
=======
  ngOnInit() {
>>>>>>> parent of 8a28f785 (filter products by category)
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

<<<<<<< HEAD
=======
      // DOM’a bağlama işlemi eksikti
>>>>>>> parent of 8a28f785 (filter products by category)
      if (this.observerElement?.nativeElement) {
        this.observer.observe(this.observerElement.nativeElement);
      }
    }
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
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
          this.skip += this.limit;
          this.applyFilter();

          if (data.length < this.limit) {
            this.allLoaded = true;
            this.observer.disconnect();
          }
        } else {
          this.allLoaded = true;
          this.observer.disconnect();
        }
        this.loading = false;
      },
      error: err => {
        console.error('🔴 Ürünler alınamadı:', err);
        this.loading = false;
        this.allLoaded = true;
        this.observer.disconnect();
      }
    });
  }

<<<<<<< HEAD
  private loadPopularProducts(): void {
    this.loading = true;
    this.productService.getProducts(this.limit, this.skip).subscribe({
      next: data => {
        this.products = [...this.products, ...data];
        this.skip += this.limit;
        this.applyFilter();

        if (data.length < this.limit) {
          this.allLoaded = true;
          this.observer.disconnect();
        }
        this.loading = false;
      },
      error: err => {
        console.error('🔴 Ürünler alınamadı:', err);
        this.loading = false;
        this.allLoaded = true;
        this.observer.disconnect();
      }
    });
  }

  filterByCategory(category: string | 'All'): void {
    if (category === 'All') {
      this.filteredProducts = [...this.products];
    } else {
      this.filteredProducts = this.products.filter(
        p =>
          p.mainCategory === category ||
          p.sideCategories?.includes(category)
      );
    }
  }

  private applyFilter(): void {
    const category = this.route.snapshot.queryParams['category'];
    if (category && category !== 'All') {
      const matchedEnum = Object.values(MainCategory).find(v => v === category);
      this.filteredProducts = this.products.filter(
        p =>
          p.mainCategory === matchedEnum ||
          p.sideCategories?.includes(category)
=======
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
>>>>>>> parent of 8a28f785 (filter products by category)
      );
    } else {
      this.filteredProducts = [...this.products];
    }
  }
  

  getSelectedSideCategories(
    category: MainCategory,
    indexes: number[]
  ): string[] {
    const all = SideCategories[category] || [];
    return indexes.map(i => all[i]).filter(Boolean);
  }

  /** artık {product, quantity} alıyor */
  onAddToCart(event: { product: Product; quantity: number }): void {
    const { product, quantity } = event;
    this.cartService.addToCart(product.id, quantity).subscribe({
<<<<<<< HEAD
      next: () =>
        console.log(`🛒 ${product.name} sepete eklendi (x${quantity}).`),
      error: err =>
        console.error('❌ Sepete eklenirken hata oluştu:', err)
    });
  }

=======
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
  
>>>>>>> parent of 8a28f785 (filter products by category)
}


