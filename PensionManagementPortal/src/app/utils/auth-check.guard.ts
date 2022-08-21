import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  // activates for URL where authorization is required
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    console.log("Runninng AUTHENTICAION GUARD!");
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl("login");
    }
    // the user can access the resources only when he is vaildated to be valid
    return this.authService.isLoggedIn();
  }

}
