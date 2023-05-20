import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {SERVER_API_URL} from "../constants";
import {map, Observable} from "rxjs";
import {AccountService} from "./account.service";
import {Login} from "../models/Login";
import {Register} from "../models/Register";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  BASE_API = SERVER_API_URL + '/auth';
  constructor(private httpClient: HttpClient, private accountService: AccountService, private router: Router) { }

  login(data: Login) {
    return this.httpClient.post<{jwtToken: string}>(this.BASE_API + '/login', data)
      .pipe(map(x => this.authenticationSuccess(x.jwtToken)));
  }

  register(data: Register) {
    return this.httpClient.post<{id: number}>(this.BASE_API + '/register', data)
      .pipe(map(_ => {}));
  }

  logout(): void {
    localStorage.removeItem('authenticationToken');
    this.accountService.clearCurrentAccount();
    this.router.navigate(['/'])
  }

  private authenticationSuccess(jwt_token: string) {
    localStorage.setItem('authenticationToken', jwt_token);
    this.accountService.loadCurrentAccount();
  }
}
