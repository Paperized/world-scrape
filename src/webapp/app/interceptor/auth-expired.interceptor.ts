import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {AccountService} from "../services/account.service";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {WS_BACKEND_URL, WS_SCRAPAPER_URL} from "../../environments/environment";
import {isWSServer} from "./interceptors";

@Injectable()
export class AuthExpiredInterceptor implements HttpInterceptor {

  constructor(private accountService: AccountService,
              private authService: AuthService,
              private router: Router) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      tap({
        error: (err: HttpErrorResponse) => {
          if(err.status === 401 && err.url && isWSServer(err.url) && this.accountService.isAuthenticated()) {
            this.authService.logout();
            this.router.navigate(['/login']);
          }
        }
      })
    );
  }
}
