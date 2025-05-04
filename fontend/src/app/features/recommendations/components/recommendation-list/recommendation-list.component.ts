import { Component, Input, OnInit } from '@angular/core';
import { Product } from '../../../../models/product.model';
import { RecommendationService } from '../../service/recommendation.service';
import { ProductService } from '../../../products/services/product.service';
import { Recommendation } from '../../../../models/recommendation.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-recommendation-list',
  standalone: false,
  templateUrl: './recommendation-list.component.html',
  styleUrl: './recommendation-list.component.css'
})
export class RecommendationListComponent implements OnInit {
  @Input() userId!: number;

  recommendations: Array<{ recommendation: Recommendation; product: Product }> = [];
  router: any;

  constructor(
    private recService: RecommendationService,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.recService.getRecommendations(this.userId).subscribe(recs => {
      const productCalls = recs.map(r => this.productService.getProductById(r.productId));
      forkJoin(productCalls).subscribe(products => {
        this.recommendations = recs.map((r, i) => ({ recommendation: r, product: products[i] }));
      });
    });
  }

  goToProduct(id: number) {
    this.router.navigate(['/products', id]);
  }
}
