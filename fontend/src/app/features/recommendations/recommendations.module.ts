import { NgModule } from "@angular/core";
import { RecommendationListComponent } from "./components/recommendation-list/recommendation-list.component";
import { CommonModule } from "@angular/common";
import { SimilarProductsComponent } from "./components/similar-products/similar-products.component";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [
    SimilarProductsComponent,
    RecommendationListComponent
  ],
  imports: [
    CommonModule,
    RouterModule
   ],
  exports: [
    SimilarProductsComponent,
    RecommendationListComponent
  ]
})
export class RecommendationsModule { }
