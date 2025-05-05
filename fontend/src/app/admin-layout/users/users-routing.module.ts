import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from '../../features/user/user/user.component';

const routes: Routes = [
  { path: '', component: UserComponent }  // artık başka route yok
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule {}
