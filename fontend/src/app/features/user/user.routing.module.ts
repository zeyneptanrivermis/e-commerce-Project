import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { UserComponent } from './user/user.component';
import { WishlistComponent } from './wishlist/wishlist.component';

const routes: Routes = [
  {path: '',
      component: UserComponent,
      children: [
        {
          path: '',
          component: UserProfileComponent
        },

        {
          path: 'wishlist',
          component: WishlistComponent
        },

      ]
    }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
