import {ErrorComponent} from "./error.component";
import {Route} from "@angular/router";

export const ERROR_ROUTES: Route[] = [
  {
    path: 'error',
    component: ErrorComponent,
    title: 'Error',
    data: {
      message: 'An unexpected error happened.'
    },
  },
  {
    path: 'accessdenied',
    title: 'Access Denied',
    data: {
      message: 'You dont have permissions to see this page :P.'
    },
    component: ErrorComponent,
  },
  {
    path: '404',
    component: ErrorComponent,
    title: 'Not Found',
    data: {
      message: 'This page does not exists D:.'
    },
  },
  {
    path: '**',
    redirectTo: '/404',
  },
]
