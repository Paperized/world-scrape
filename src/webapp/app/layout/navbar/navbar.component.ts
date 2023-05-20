import {Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {MatSidenav} from "@angular/material/sidenav";
import {AccountService} from "../../services/account.service";
import {Account} from "../../models/Account";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  StoreName: string = "World Scrape";
  currentAccount: Readonly<Account> | null = null;
  @Input() sidenav?: MatSidenav;

  @ViewChild("sidenavTemplate")
  sidenavTemplate?: TemplateRef<any>;

  constructor(private accountService: AccountService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.accountService.currentAccount$.subscribe(x => this.currentAccount = x);
  }

  logout() {
    this.authService.logout();
  }
}
