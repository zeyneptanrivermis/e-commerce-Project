import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderComponent } from './pages/order.component';
import { SharedModule } from '../../shared/shared.module';
import { OrderRoutingModule } from './order-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaymentComponent } from './components/payment/payment.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { ShipmentComponent } from './components/Shipment/Shipment.component';

@NgModule({
  declarations: [
    OrderComponent,
    OrderHistoryComponent,
    PaymentComponent,
    ShipmentComponent
  ],
  imports: [
    CommonModule,
    OrderRoutingModule,
    ReactiveFormsModule,
    SharedModule,
    FormsModule,
    HttpClientModule
  ],
  exports: [
   ShipmentComponent
  ]
})
export class OrderModule { }
