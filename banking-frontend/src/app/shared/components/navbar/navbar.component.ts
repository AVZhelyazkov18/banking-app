import { Component } from '@angular/core';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  constructor(public authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}
