import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MainCategory, Product } from '../../models/product.model';
@Component({
  selector: 'app-product-item',
  standalone: false,
  templateUrl: './product-item.component.html',
  styleUrl: './product-item.component.css'
})
export class ProductItemComponent {

  @Input() product!: Product;
  @Output() add = new EventEmitter<Product>();

  constructor(){

  }
  onAdd() {
    this.add.emit(this.product);
  }
}
