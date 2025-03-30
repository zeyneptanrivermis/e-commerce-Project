import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductItemComponent } from './components/product-item/product-item.component';
import { RouterModule } from '@angular/router';
import { ProductsComponent } from './pages/products/products.component';




@NgModule({
  declarations: [
    ProductsComponent,
    ProductListComponent,
    ProductItemComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    ProductListComponent,
    ProductItemComponent
  ]
})

export class ProductsModule { }
