import {Component, Input, TemplateRef, ViewChild} from '@angular/core';
import {MatSidenav} from "@angular/material/sidenav";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  StoreName: string = "AmazingShop";
  @Input() sidenav?: MatSidenav;

  @ViewChild("sidenavTemplate")
  sidenavTemplate?: TemplateRef<any>;
}
