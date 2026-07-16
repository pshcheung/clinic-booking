import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {
  ApplicationConfig,
  importProvidersFrom,
  provideBrowserGlobalErrorListeners,
//  provideZonelessChangeDetection
} from '@angular/core';
import {provideRouter, withComponentInputBinding, withInMemoryScrolling} from '@angular/router';
import {provideTranslateService} from '@ngx-translate/core';
import {provideTranslateHttpLoader} from '@ngx-translate/http-loader';

import { provideOAuthClient } from 'angular-oauth2-oidc';
import {INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, includeBearerTokenInterceptor} from 'keycloak-angular';

import { routes } from './app.routes';
import {provideKeycloakAngular} from './auth/keycloak.config';

export const appConfig: ApplicationConfig = {
  providers: [
//    provideZonelessChangeDetection(),
    provideBrowserGlobalErrorListeners(),
    provideKeycloakAngular(),
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),  // Add the configured interceptor
/*    {
      provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
      useValue: [
        {
          urlPattern: /^http:\/\/localhost\/.*$/,
          httpMethods: ['GET', 'POST', 'PUT'] // Token added only for GET and POST
        }
      ]
    },
    provideOAuthClient(),*/
    provideRouter(
      routes,
      withInMemoryScrolling({
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled',
      }),
      withComponentInputBinding()
    ),
    provideTranslateService({
      lang: 'en',
      fallbackLang: 'en',
      loader: provideTranslateHttpLoader({
        prefix: '/i18n/',
        suffix: '.json'
      })
    }),
  ]
};
