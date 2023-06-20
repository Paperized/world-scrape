import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {JwtInterceptor} from "./jwt.interceptor";
import {AuthExpiredInterceptor} from "./auth-expired.interceptor";
import {WS_BACKEND_URL, WS_SCRAPAPER_URL} from "../../environments/environment";

export function isWSServer(url: string): boolean {
  return url.startsWith(WS_BACKEND_URL) || url.startsWith(WS_SCRAPAPER_URL);
}

export const customInterceptors = [
  {
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true,
  },
  {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthExpiredInterceptor,
    multi: true,
  }
]
