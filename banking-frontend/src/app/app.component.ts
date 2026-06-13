import { Component, OnInit } from '@angular/core';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'banking-frontend';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.me().subscribe();
  }
}
