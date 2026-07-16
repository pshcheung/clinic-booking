
import { AuthConfig } from 'angular-oauth2-oidc';

export const authCodeFlowConfig: AuthConfig = {
  issuer: 'http://localhost:8080/auth/realms/clinic-booking',
  redirectUri: window.location.origin + '/index.html',
  clientId: 'clinic-client',
  responseType: 'code',
  scope: 'openid profile email offline_access',
  showDebugInformation: true,
};
