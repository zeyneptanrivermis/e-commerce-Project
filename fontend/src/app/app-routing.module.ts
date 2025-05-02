import { UserModule } from './features/user/user.module';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { AuthGuard } from './core/guards/auth-guard.service';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { HomeComponent } from './features/home/pages/home/home.component';
import { AuthLayoutComponent } from './features/auth/auth-layout/auth-layout.component';
import { UserComponent } from './features/user/user/user.component';


const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },

  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent }
    ]
  },

  { path: 'products', loadChildren: () => import('./features/products/products.module').then(m => m.ProductsModule) },

  { path: 'shopping-cart', canActivate:[AuthGuard], loadChildren: () => import('./features/cart/cart.module').then(m => m.CartModule) },
  
  { path: 'user', canActivate:[AuthGuard], loadChildren: () => import('./features/user/user.module').then(m => m.UserModule)},
  
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
