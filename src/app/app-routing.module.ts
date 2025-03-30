import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { AuthGuard } from './core/guards/auth-guard.service';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { HomeComponent } from './features/home/pages/home/home.component';
import { ProductsComponent } from './features/products/pages/products/products.component';


const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },   // Ana yol login'e yönlenir
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent },
  { path: 'products', component: ProductsComponent },

  // Boş path için otomatik yönlendirme
  { path: '', redirectTo: '/home', pathMatch: 'full' },

  // Tanımsız path’ler için fallback
  { path: '**', redirectTo: '/home' }
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
