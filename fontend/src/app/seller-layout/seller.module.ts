import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SellerDashboardComponent } from './pages/seller-dashboard/seller-dashboard.component';
import { SellerRegisterComponent } from './pages/seller-register/seller-register.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SellerRoutingModule } from './seller-routing.module';
import { SellerProductsComponent } from './pages/seller-products/seller-products.component';
import { SellerOrdersComponent } from './pages/seller-orders/seller-orders.component';
import { SellerMenuComponent } from './seller-menu/seller-menu.component';
import { ManageProductsComponent } from '../features/products/pages/manage-products/manage-products.component';


@NgModule({
  declarations: [
    SellerRegisterComponent,
    SellerDashboardComponent,
    SellerProductsComponent,
    SellerOrdersComponent,
    ManageProductsComponent,
    SellerMenuComponent
  ],
  imports: [
    ReactiveFormsModule,
    CommonModule,
    SellerRoutingModule
  ]
})
export class SellerModule { }
