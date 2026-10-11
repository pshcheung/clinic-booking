import {
  provideKeycloak,
  createInterceptorCondition,
  IncludeBearerTokenCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  withAutoRefreshToken,
  AutoRefreshTokenService,
  UserActivityService
} from 'keycloak-angular';
import {environment} from '../../environments/environment';

const apiBaseUrl = environment.apiURL.replace(/\/+$/, '');
const escapedApiBaseUrl = apiBaseUrl.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
const apiCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: new RegExp(`^${escapedApiBaseUrl}(?:/|$)`, 'i'),
});

export const provideKeycloakAngular = () =>
  provideKeycloak({
    config: {
//      realm: 'keycloak-angular-sandbox',
      realm: 'clinic-booking',
      url: 'http://localhost:8080',
//      clientId: 'keycloak-angular'
      clientId: 'clinic-one-client'
    },
    initOptions: {
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      redirectUri: window.location.origin + '/'
    },
    features: [
      withAutoRefreshToken({
        onInactivityTimeout: 'logout',
        sessionTimeout: 1000
      })
    ],
    providers: [
      AutoRefreshTokenService,
      UserActivityService,
      {
        provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
        useValue: [apiCondition]
      }
    ]
  });
