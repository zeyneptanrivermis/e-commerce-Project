import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { WishlistComponent } from './components/wishlist/wishlist.component';
import { ShippingComponent } from './components/shipping/shipping.component';
import { ProfileHomeComponent } from './pages/profile-home/profile-home.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';

const routes: Routes = [
  {
    path: '',
    component: ProfileHomeComponent
  },
  {
    path: 'user-profile',
    component: UserProfileComponent
  },
  {
    path: 'wishlist',
    component: WishlistComponent
  },
  {
    path: 'shipping',
    component: ShippingComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})

export class UserRoutingModule { }
