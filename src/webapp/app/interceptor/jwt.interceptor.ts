import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import {SERVER_API_URL} from "../constants";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor() {}
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (request.url.startsWith(SERVER_API_URL)) {
      const jwtToken = localStorage.getItem('authenticationToken');
      if(jwtToken != null) {
        request = request.clone({
          headers: request.headers.set('Authorization', 'Bearer ' + jwtToken)
        });
      }
    }

    return next.handle(request);
  }
}
