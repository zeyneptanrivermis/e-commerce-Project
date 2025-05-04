import { inject, NgModule, PLATFORM_ID } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

// Global modüller
import { LayoutModule } from './layout/layout.module';
import { SharedModule } from './shared/shared.module';

// Angular Material örneği (isteğe bağlı)

import { AuthModule } from './features/auth/auth.module';
//import { ProductsComponent } from './features/products/pages/products/products.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptors } from '@angular/common/http';
import { UserComponent } from './features/user/user/user.component';
import { UserModule } from './features/user/user.module';
import { TokenService } from './core/services/token.service';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { ReactiveFormsModule } from '@angular/forms';
import { HomeModule } from './features/home/home.module';
import { RecommendationsModule } from './features/recommendations/recommendations.module';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    AuthModule,
    LayoutModule,
    HomeModule,
    SharedModule,
    AppRoutingModule,
    UserModule,
    RecommendationsModule
  ],
  providers: [
    provideClientHydration(withEventReplay()),

    // ✅ Interceptor burada Angular 17+ ile doğru şekilde tanıtılıyor
    provideHttpClient(
      withInterceptors([authInterceptor])),
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
