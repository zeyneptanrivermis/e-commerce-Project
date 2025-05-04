import { Component, Input, OnInit } from '@angular/core';
import { Product } from '../../../../models/product.model';
import { RecommendationService } from '../../service/recommendation.service';
import { ProductService } from '../../../products/services/product.service';
import { Recommendation } from '../../../../models/recommendation.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-similar-products',
  standalone: false,
  templateUrl: './similar-products.component.html',
  styleUrl: './similar-products.component.css'
})
export class SimilarProductsComponent implements OnInit {
  @Input() productId!: number;
  router: any;

  similarList: Array<{ recommendation: Recommendation; product: Product }> = [];

  constructor(
    private recService: RecommendationService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.recService.getSimilarProducts(this.productId).subscribe(recs => {
      const productCalls = recs.map(r => this.productService.getProductById(r.productId));
      forkJoin(productCalls).subscribe(products => {
        this.similarList = recs.map((r, i) => ({ recommendation: r, product: products[i] }));
      });
    });
  }

  goToProduct(id: number) {
    this.router.navigate(['/products', id]);
  }
}
