import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsComponent } from './pages/products/products.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { ManageProductsComponent } from './pages/manage-products/manage-products.component';


const routes: Routes = [
  {
    path: '',
    component: ProductsComponent,
    children: [
      {
        path: '',
        component: ProductListComponent
      },
      {
        path: 'popularProducts',
        component: ProductListComponent // istersen farklı bir component de koyabiliriz
      },
      {
        path: 'manage',
        component: ManageProductsComponent
      },
      {
        path: ':id',
        component: ProductDetailsComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProductsRoutingModule {}