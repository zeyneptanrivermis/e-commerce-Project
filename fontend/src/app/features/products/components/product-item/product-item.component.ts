import { AuthGuard } from './../../../../core/guards/auth-guard.service';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../../../models/product.model';
import { PopularityService } from '../../services/popular-products/product-popularity.service';

@Component({
  selector: 'app-product-item',
  standalone: false,
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.css'
})
export class ProductItemComponent {

  @Input() product!: Product;
  @Output() add  = new EventEmitter<Product>();
  private productPopularity: number= 0;

  constructor(private popularity: PopularityService, private authguard: AuthGuard){}

  onAdd() {
    this.add.emit(this.product);
    this.popularity.increment(this.productPopularity);
  }

}
