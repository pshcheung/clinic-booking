import {HttpClient, provideHttpClient, withInterceptors} from '@angular/common/http';
import {
  ApplicationConfig,
  inject,
  provideBrowserGlobalErrorListeners,
  provideAppInitializer,
//  provideZonelessChangeDetection
} from '@angular/core';
import {provideRouter, withComponentInputBinding, withInMemoryScrolling} from '@angular/router';
import {provideTranslateService} from '@ngx-translate/core';
import {provideTranslateHttpLoader} from '@ngx-translate/http-loader';
import {firstValueFrom} from 'rxjs';

import {includeBearerTokenInterceptor} from 'keycloak-angular';

import { routes } from './app.routes';
import {provideKeycloakAngular} from './auth/keycloak.config';
import {environment} from '../environments/environment';
import {ApplicationData} from './models/application-data';
import {ApplicationDataStore} from './stores/application-data.store';

export const appConfig: ApplicationConfig = {
  providers: [
//    provideZonelessChangeDetection(),
    provideBrowserGlobalErrorListeners(),
    provideKeycloakAngular(),
    provideAppInitializer(() => {
      const http = inject(HttpClient);
      const applicationDataStore = inject(ApplicationDataStore);
      return firstValueFrom(
        http.get<ApplicationData>(`${environment.apiURL}/api/public/application-data`),
      ).then(applicationData => applicationDataStore.setApplicationData(applicationData));
    }),
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
