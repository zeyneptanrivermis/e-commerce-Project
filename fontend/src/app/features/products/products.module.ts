import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductItemComponent } from './components/product-item/product-item.component';
import { RouterModule } from '@angular/router';
import { ProductsComponent } from './pages/products/products.component';
import { ProductsRoutingModule } from './products-routing.module';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ManageProductsComponent } from './pages/manage-products/manage-products.component';
import { ReactiveFormsModule } from '@angular/forms';




@NgModule({
  declarations: [
    ProductsComponent,
    ProductListComponent,
    ProductItemComponent,
    ProductDetailsComponent,
    ManageProductsComponent,

  ],
  imports: [
    CommonModule,
    ProductsRoutingModule,
    RouterModule,
    ReactiveFormsModule
  ],
  exports: [
  ],
  providers: []
})

export class ProductsModule { }
