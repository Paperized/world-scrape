import {Component, OnInit} from '@angular/core';
import {AccountService} from "../../services/account.service";
import {PageModeService} from "../../services/page-mode.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  maintainScreenHeight: boolean = false;

  constructor(private accountService: AccountService, private pageMode: PageModeService) { }

  ngOnInit(): void {
    // load current account if authenticated and not in memory
    this.accountService.loadCurrentAccount();
    this.pageMode.getMaintainScreenHeight().subscribe({ next: v => this.maintainScreenHeight = v });
  }
}
