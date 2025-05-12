import { NgModule } from '@angular/core';
import { HomeComponent } from './pages/home/home.component';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@NgModule({
    declarations: [
        HomeComponent
    ],
    imports: [
        CommonModule,
        RouterModule

    ],
    exports: [
        HomeComponent
    ]
  })

  export class HomeModule{

  }
