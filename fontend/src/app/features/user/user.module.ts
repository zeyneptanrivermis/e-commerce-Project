import { User } from './../../models/user.model';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistComponent } from './components/wishlist/wishlist.component';

import { CartModule } from '../cart/cart.module';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import { ProfileHomeComponent } from './pages/profile-home/profile-home.component';
import { ShippingComponent } from './components/shipping/shipping.component';


@NgModule({
  declarations: [
    ProfileHomeComponent,
    UserProfileComponent,
    WishlistComponent,
    ShippingComponent
  ],
  imports: [
    CommonModule,
    CartModule
  ]
})
export class UserModule { }
