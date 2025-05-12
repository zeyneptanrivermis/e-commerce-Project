import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth-guard.service';    // Giriş yapmış mı kontrol
import { AdminComponent } from './admin.component';
import { RefundListComponent } from './RefundList/RefundList.component';

const routes: Routes = [
  {
    path: '',
    component: AdminComponent,
    canActivate: [AuthGuard],
    children: [
      // Admin ana sayfa olarak dashboard’a yönlendir
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

      // Dashboard modülü
      {
        path: 'dashboard',
        loadChildren: () =>
          import('./dashboard/dashboard.module')
            .then(m => m.DashboardModule)
      },

      // Kullanıcı yönetimi modülü
      {
        path: 'users',
        loadChildren: () =>
          import('./users/users.module')
            .then(m => m.UsersModule)
      },

      // Ürün yönetimi modülü
      {
        path: 'products',
        loadChildren: () =>
          import('./products/products.module')
            .then(m => m.ProductsModule)
      },

      // Sipariş yönetimi modülü
      {
        path: 'orders',
        loadChildren: () =>
          import('./orders/orders.module')
            .then(m => m.OrdersModule)
      },

      // Destek modülü
      {
        path: 'support',
        loadChildren: () =>
          import('./support/support.module')
            .then(m => m.SupportModule)
      },
      { path: 'refunds', component: RefundListComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
