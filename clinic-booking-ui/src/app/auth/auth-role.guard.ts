import {AuthGuardData, createAuthGuard} from 'keycloak-angular';
import { ActivatedRouteSnapshot, CanActivateFn, RouterStateSnapshot, UrlTree } from '@angular/router';
import { inject } from '@angular/core';
import Keycloak from 'keycloak-js';

/**
 * The logic below is a simple example, please make it more robust when implementing in your application.
 *
 * Reason: isAccessGranted is not validating the resource, since it is merging all roles. Two resources might
 * have the same role name and it makes sense to validate it more granular.
 */
const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  __: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const keycloak = inject(Keycloak);
  const { authenticated, grantedRoles } = authData;

  if (!authenticated) {
    await keycloak.login({
      redirectUri: window.location.href,
    });
  }

  const requiredRoles = route.data['role'];
  if (!requiredRoles) {
    return false;
  }

  const hasRequiredRole = (roles: string[]): boolean => {
    return roles.some((role) => grantedRoles.realmRoles.includes(role));
  }

  const isBelongToOrganization = (): boolean => {
    const parsedToken = keycloak.tokenParsed;
    const organizations = parsedToken?.['organization'];
    const requiredOrganization = route.data['organization'] as string | undefined;
    if (!requiredOrganization) return true;
    if (Array.isArray(organizations)) return organizations.includes(requiredOrganization);
    if (!organizations || typeof organizations !== 'object') return false;
    return Object.entries(organizations).some(([name, organization]) =>
      name === requiredOrganization || organization === requiredOrganization ||
      (organization !== null && typeof organization === 'object' &&
        Object.values(organization as Record<string, unknown>).includes(requiredOrganization))
    );
  }

  return authenticated && hasRequiredRole(requiredRoles) && isBelongToOrganization();

/*  const router = inject(Router);
  return router.parseUrl('/forbidden');*/
};

export const canActivateAuthRole = createAuthGuard<CanActivateFn>(isAccessAllowed);
