import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SellerDashboardComponent } from './pages/seller-dashboard/seller-dashboard.component';
import { ManageProductsComponent } from '../features/products/pages/manage-products/manage-products.component';
import { SellerOrdersComponent } from './pages/seller-orders/seller-orders.component';
import { SellerProductsComponent } from './pages/seller-products/seller-products.component';


const routes: Routes = [
    { path: '', redirectTo:'dashboard', pathMatch: 'full'},
    { path: 'dashboard', component: SellerDashboardComponent },
    { path: 'products', component: SellerProductsComponent },
    { path: 'orders', component: SellerOrdersComponent },
    { path: 'products/manage', component: ManageProductsComponent},

  ];
  

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SellerRoutingModule { }
