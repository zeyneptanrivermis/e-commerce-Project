import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from './core/guards/auth-guard.service';
import { AuthLayoutComponent } from './features/auth/auth-layout/auth-layout.component';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { HomeComponent } from './features/home/pages/home/home.component';
import { SellerRegisterComponent } from './seller-layout/pages/seller-register/seller-register.component';

const routes: Routes = [
  // 1) Anasayfa
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },

  // 2) Auth (login/register)
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: 'login',    component: LoginComponent },
      { path: 'register', component: RegisterComponent }
    ]
  },

  // 3) Ürün listeleme (herkes görebilir)
  {
    path: 'products',
    loadChildren: () => import('./features/products/products.module')
                          .then(m => m.ProductsModule)
  },

  { path: 'shopping-cart', canActivate:[AuthGuard], loadChildren: () => import('./features/cart/cart.module').then(m => m.CartModule),    data: { roles: ['ROLE_CUSTOMER'] } },

  // 5) Kullanıcı profili (sadece müşteri)
  {
    path: 'user',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_CUSTOMER'] },
    loadChildren: () => import('./features/user/user.module')
                          .then(m => m.UserModule)
  },

  // 6) Siparişler (sadece müşteri)
  {
    path: 'order',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_CUSTOMER'] },
    loadChildren: () => import('./features/order/order.module')
                          .then(m => m.OrderModule)
  },

  // 7) Admin (sadece admin, navbar/footer gizli olsun)
  {
    path: 'admin',
    canActivate: [AuthGuard],
    data: { roles: ['ROLE_ADMIN'], hideLayout: true },
    loadChildren: () => import('./admin-layout/admin-layout.module')
                          .then(m => m.AdminLayoutModule)
  },
    {
      path: 'seller/register',
      component: SellerRegisterComponent
    },
    {
      path: 'seller',
      canActivate: [AuthGuard],
      data: { roles: ['ROLE_SELLER'], hideLayout: true },
      loadChildren: () => import('./seller-layout/seller.module').then(m => m.SellerModule)
    },
  // 8) Yakalanamayan tüm rotalar
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
