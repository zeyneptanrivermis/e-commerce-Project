import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderComponent } from './pages/order.component';
import { PaymentComponent } from './components/payment/payment.component';
import { ShipmentComponent } from './components/Shipment/Shipment.component';

const routes: Routes = [
  { path: '', component: OrderComponent },                 // Sipariş oluşturma
  { path: 'payment', component: PaymentComponent},
  { path: 'shipment/:orderId', component: ShipmentComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule { }
