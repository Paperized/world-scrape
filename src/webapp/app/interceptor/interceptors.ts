import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {JwtInterceptor} from "./jwt.interceptor";
import {AuthExpiredInterceptor} from "./auth-expired.interceptor";

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
