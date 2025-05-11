import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UserComponent } from './user/user.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { OrderHistoryComponent } from '../order/components/order-history/order-history.component';
import { ShipmentComponent } from '../order/components/Shipment/Shipment.component';

const routes: Routes = [
  {
    path: '',
    component: UserComponent,
    children: [
      { path: 'user-profile', component: UserProfileComponent },
      { path: 'wishlist', component: WishlistComponent },
      { path: 'order-history', component: OrderHistoryComponent},
      { path: 'shipment/:orderId', component: ShipmentComponent },
      { path: '', redirectTo: 'user-profile', pathMatch: 'full' } // opsiyonel: boşken profil aç
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
