import { Injectable } from '@angular/core';
import {SERVER_API_URL} from "../constants";
import {HttpClient} from "@angular/common/http";
import {Account} from "../models/Account";
import {BehaviorSubject, Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  BASE_API = SERVER_API_URL + '/user';
  private _currentAccount: Readonly<Account> | null = null;
  currentAccount$ = new BehaviorSubject<Readonly<Account> | null>(null);

  constructor(private httpClient: HttpClient) { }

  clearCurrentAccount() {
    this._currentAccount = null;
    this.currentAccount$.next(null);
  }

  loadCurrentAccount(): void {
    if(this._currentAccount != null || !localStorage.getItem('authenticationToken')) {
      return;
    }

    this.fetchCurrentAccount().subscribe(x => {
      this._currentAccount = x;
      this.currentAccount$.next(this._currentAccount);
    });
  }

  isAuthenticated(): boolean {
    return this._currentAccount !== null || localStorage.getItem('authenticationToken') !== null;
  }

  private fetchCurrentAccount(): Observable<Account> {
    return this.httpClient.get<Account>(this.BASE_API + '/current');
  }

  get currentAccount() {
    return this._currentAccount;
  }
}
