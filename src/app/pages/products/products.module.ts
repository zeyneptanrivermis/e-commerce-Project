import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ProductsRoutingModule } from './products-routing.module';
import { ProductListComponent } from '../../components/product-list/product-list.component';
import { ProductItemComponent } from '../../components/product-item/product-item.component';


@NgModule({
  declarations: [
    ProductListComponent,
    ProductItemComponent
  ],
  imports: [
    CommonModule,
    ProductsRoutingModule
  ],
  exports: [
    ProductListComponent,
    ProductItemComponent
  ]
})
export class ProductsModule { }
