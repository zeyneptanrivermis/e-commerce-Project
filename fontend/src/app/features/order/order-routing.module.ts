import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderComponent } from './pages/order.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';

const routes: Routes = [
  { path: '', component: OrderComponent },                 // Sipariş oluşturma
  { path: 'order-history', component: OrderHistoryComponent },   // Sipariş geçmişi
  { path: 'order-detail', component: OrderDetailComponent },        // Sipariş detayı
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
