import { User } from './../../models/user.model';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WishlistComponent } from './wishlist/wishlist.component';
import { UserComponent } from './user/user.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { CartModule } from '../cart/cart.module';


@NgModule({
  declarations: [
    WishlistComponent,
    UserComponent,
    UserProfileComponent,
  ],
  imports: [
    CommonModule,
    CartModule
  ]
})
export class UserModule { }
