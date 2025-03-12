import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'footer', pathMatch: 'full' }, // Ana sayfa yönlendirmesi
  { path: 'footer', loadChildren: () => import('./components/footer/footer.module').then(m => m.FooterModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
