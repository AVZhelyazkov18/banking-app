import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    const requiredRoles: string[] = route.data['roles'];
    if (requiredRoles && requiredRoles.length > 0) {
      const userRole = this.authService.getRole();
      if (!userRole || !requiredRoles.includes(userRole)) {
        this.router.navigate(['/login']);
        return false;
      }
    }
    return true;
  }
}
