// src/app/core/interceptors/auth.interceptor.ts
import {
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn
} from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { TokenService } from '../services/token.service';

export const authInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
) => {
  const tokenService = inject(TokenService);
  const platformId = inject(PLATFORM_ID);

  console.log('✅ AuthInterceptor (function) çalıştı!');

  if (isPlatformBrowser(platformId)) {
    const token = tokenService.getToken();
    console.log('Token from TokenService (client):', token);

    if (token) {
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next(authReq);
    }
  } else {
    console.warn('🚫 Token erişimi sunucu tarafında engellendi (SSR)');
  }

  return next(req);
};
