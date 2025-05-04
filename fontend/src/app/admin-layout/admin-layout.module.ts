import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin/admin/admin.component';
import { AdminRoutingModule } from './admin-routing.module';
import { RouterModule } from '@angular/router';



@NgModule({
  declarations: [
    AdminComponent,
    
  ],
  imports: [
    CommonModule,
    RouterModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
