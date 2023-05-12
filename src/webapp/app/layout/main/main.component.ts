import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../services/account.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  constructor(private accountService: AccountService) { }

  ngOnInit(): void {
    // load current account if authenticated and not in memory
    this.accountService.loadCurrentAccount();
  }
}
