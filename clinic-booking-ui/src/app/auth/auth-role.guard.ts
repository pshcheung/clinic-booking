import {AuthGuardData, createAuthGuard} from 'keycloak-angular';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
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
    return Object.values(grantedRoles.realmRoles).some((roles) => roles.includes(roles));
  }

  const isBelongToOrganization = async (): Promise<boolean> => {
    // Get the user's token and decode it to access claims
    const parsedToken = keycloak.tokenParsed;

    // Access the organization claim (the claim name depends on your Keycloak setup)
    const userOrganizations = parsedToken ? parsedToken['organization'] : null; // Replace 'organization_id' with your actual claim name
    const requiredOrganization = route.data['organization'];

    // Check if the user's organization matches the required organization for the route
    return Object.values(userOrganizations).some((org) => org === requiredOrganization);
  }

  return authenticated && hasRequiredRole(requiredRoles) && await isBelongToOrganization();

/*  const router = inject(Router);
  return router.parseUrl('/forbidden');*/
};

export const canActivateAuthRole = createAuthGuard<CanActivateFn>(isAccessAllowed);
