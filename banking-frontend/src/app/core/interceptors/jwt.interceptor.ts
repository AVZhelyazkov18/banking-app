import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  private refreshing = false;
  private refreshDone$ = new BehaviorSubject<boolean>(false);
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private router: Router, private http: HttpClient) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const withCreds = req.clone({ withCredentials: true });

    return next.handle(withCreds).pipe(
      catchError((err: HttpErrorResponse) => {
        // Do not attempt refresh on auth endpoints
        if (err.status === 401 && !req.url.includes('/api/auth/')) {
          return this.handleUnauthorized(withCreds, next);
        }
        return throwError(() => err);
      })
    );
  }

  private handleUnauthorized(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.refreshing) {
      return this.refreshDone$.pipe(
        filter(done => done),
        take(1),
        switchMap(() => next.handle(req))
      );
    }

    this.refreshing = true;
    this.refreshDone$.next(false);

    return this.http.post(`${this.apiUrl}/refresh`, {}, { withCredentials: true }).pipe(
      switchMap(() => {
        this.refreshing = false;
        this.refreshDone$.next(true);
        return next.handle(req);
      }),
      catchError(err => {
        this.refreshing = false;
        this.refreshDone$.next(true);
        this.router.navigate(['/login']);
        return throwError(() => err);
      })
    );
  }
}
