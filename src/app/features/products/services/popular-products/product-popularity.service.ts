import { Injectable } from '@angular/core';
import { Product } from '../../../../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class PopularityService {
  private popularityMap = new Map<number, number>(); // productId → count

  increment(productId: number): void {
    const current = this.popularityMap.get(productId) || 0;
    this.popularityMap.set(productId, current + 1);
  }

  getTopPopularProducts(allProducts: Product[], count: number = 20): Product[] {
    const sorted = [...allProducts].sort((a, b) => {
      const popA = this.popularityMap.get(a.id) || 0;
      const popB = this.popularityMap.get(b.id) || 0;
      return popB - popA;
    });
    return sorted.slice(0, count);
  }
}
