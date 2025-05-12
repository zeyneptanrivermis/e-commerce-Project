import { User } from './../../models/user.model';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistComponent } from './wishlist/wishlist.component';
import { UserComponent } from './user/user.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { CartModule } from '../cart/cart.module';
import { RouterModule } from '@angular/router';
import { UserRoutingModule } from './user.routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddressModule } from '../address/address.module';
import { AddressComponent } from '../address/address-component/address.component';
import { OrderModule } from '../order/order.module';
import { OrderHistoryComponent } from '../order/components/order-history/order-history.component';


@NgModule({
  declarations: [
    WishlistComponent,
    UserComponent,
    UserProfileComponent,
    AddressComponent,
    // OrderHistoryComponent
  ],
  imports: [
    CommonModule,
    CartModule,
    RouterModule,
    UserRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    OrderModule,
  ]
})
export class UserModule { }
