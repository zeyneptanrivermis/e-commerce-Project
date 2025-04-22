import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

// Global modüller
import { LayoutModule } from './layout/layout.module';
import { SharedModule } from './shared/shared.module';

// Angular Material örneği (isteğe bağlı)
import { MatIconModule } from '@angular/material/icon';
import { AuthModule } from './features/auth/auth.module';
//import { ProductsComponent } from './features/products/pages/products/products.component';
import { provideHttpClient } from '@angular/common/http';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    MatIconModule,

    // Uygulama modülleri
    AuthModule,
    //ProductsModule,
    LayoutModule,                // Navbar, footer gibi layout bileşenleri
    SharedModule,                // Ortak bileşenler, pipe, directive
    AppRoutingModule          // Route’lar burada tanımlı
  ],
  providers: [
    provideClientHydration(withEventReplay()),
    provideHttpClient()
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
 }
