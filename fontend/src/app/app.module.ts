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
import { HTTP_INTERCEPTORS, provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { UserComponent } from './features/user/user/user.component';
import { UserModule } from './features/user/user.module';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { ReactiveFormsModule } from '@angular/forms';
import { HomeModule } from './features/home/home.module';
import { RecommendationsModule } from './features/recommendations/recommendations.module';
import { ChatModule } from './features/chat/chat.module';

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
    RecommendationsModule,
    ChatModule
  ],
  providers: [
    provideClientHydration(withEventReplay()),

    provideHttpClient(
      withFetch(),
      withInterceptors([authInterceptor])),
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
