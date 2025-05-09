import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductItemComponent } from './components/product-item/product-item.component';
import { RouterModule } from '@angular/router';
import { ProductsComponent } from './pages/products/products.component';
import { ProductsRoutingModule } from './products-routing.module';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RecommendationListComponent } from '../recommendations/components/recommendation-list/recommendation-list.component';
import { RecommendationsModule } from '../recommendations/recommendations.module';




@NgModule({
  declarations: [
    ProductsComponent,
    ProductListComponent,
    ProductItemComponent,
    ProductDetailsComponent,
  ],
  imports: [
    CommonModule,
    ProductsRoutingModule,
    RouterModule,
    ReactiveFormsModule,
    RecommendationsModule
  ],
  exports: [
    ProductItemComponent
  ],
  providers: []
})

export class ProductsModule { }
