import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { ReactiveFormsModule } from '@angular/forms';
import { UsersComponent } from './users/users.component';
import { SupportComponent } from './support/support.component';
import { OrdersComponent } from './orders/orders.component';
import { AdminComponent } from './admin.component';
import { ProductsModule } from './products/products.module';
import { DashboardModule } from './dashboard/dashboard.module';
import { RouterModule } from '@angular/router';
import { UsersModule } from './users/users.module';

@NgModule({
  declarations: [
    AdminComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    ReactiveFormsModule,
    DashboardModule,
    UsersModule,
    RouterModule,
    ProductsModule
  ]
})
export class AdminLayoutModule { }
