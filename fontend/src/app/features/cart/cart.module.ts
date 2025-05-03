import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CartComponent } from './pages/cart/cart.component';
import { CartRoutingModule } from './cart-routing.module';

@NgModule({
    declarations: [
      CartComponent
  ],
    imports: [
      CommonModule,
      CartRoutingModule
    ],
    exports: [
    ]
  })

  export class CartModule{

  }
