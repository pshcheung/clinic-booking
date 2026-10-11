import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
//  provideZonelessChangeDetection
} from '@angular/core';
import {provideRouter, withComponentInputBinding, withInMemoryScrolling} from '@angular/router';
import {provideTranslateService} from '@ngx-translate/core';
import {provideTranslateHttpLoader} from '@ngx-translate/http-loader';

import {includeBearerTokenInterceptor} from 'keycloak-angular';

import { routes } from './app.routes';
import {provideKeycloakAngular} from './auth/keycloak.config';

export const appConfig: ApplicationConfig = {
  providers: [
//    provideZonelessChangeDetection(),
    provideBrowserGlobalErrorListeners(),
    provideKeycloakAngular(),
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),
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
