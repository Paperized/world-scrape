import {Route} from "@angular/router";
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {ScraperExecutionComponent} from "./scraper-execution/scraper-execution.component";
import {ScraperCreationComponent} from "./scraper-creation/scraper-creation.component";
import {authGuard} from "../guard/auth.guard";

export const PAGES_ROUTES: Route[] = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component:  RegisterComponent
  },
  {
    path: 'scraper',
    component: ScraperExecutionComponent
  },
  {
    path: 'scraper-creation',
    component: ScraperCreationComponent,
    canActivate: [authGuard]
  }
]
