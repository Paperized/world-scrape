import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Account} from "../models/Account";
import {BehaviorSubject, Observable} from "rxjs";
import {WS_BACKEND_URL} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  BASE_API = WS_BACKEND_URL + '/users';
  private _currentAccount: Readonly<Account> | null = null;
  currentAccount$ = new BehaviorSubject<Readonly<Account> | null>(null);

  constructor(private httpClient: HttpClient) {
  }

  clearCurrentAccount() {
    this._currentAccount = null;
    this.currentAccount$.next(null);
  }

  loadCurrentAccount(): void {
    if (this._currentAccount != null || !localStorage.getItem('authenticationToken')) {
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
