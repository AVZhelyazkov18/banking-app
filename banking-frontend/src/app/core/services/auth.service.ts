import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequest, RegisterRequest, AuthResponse } from '../../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  private username: string | null = null;
  private role: string | null = null;

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, req, { withCredentials: true }).pipe(
      tap(response => {
        this.username = response.username;
        this.role = response.role;
      })
    );
  }

  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, req, { withCredentials: true }).pipe(
      tap(response => {
        this.username = response.username;
        this.role = response.role;
      })
    );
  }

  logout(): void {
    this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).subscribe({
      complete: () => {
        this.username = null;
        this.role = null;
        this.router.navigate(['/login']);
      }
    });
  }

  me(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/me`, { withCredentials: true }).pipe(
      tap(response => {
        this.username = response.username;
        this.role = response.role;
      })
    );
  }

  isLoggedIn(): boolean {
    return this.username !== null;
  }

  getRole(): string | null {
    return this.role;
  }

  getUsername(): string | null {
    return this.username;
  }
}
