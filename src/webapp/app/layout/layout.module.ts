import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MainComponent} from "./main/main.component";
import {NavbarComponent} from "./navbar/navbar.component";
import {FooterComponent} from "./footer/footer.component";
import {RouterModule} from "@angular/router";
import {SharedModule} from "../shared/shared.module";
import { ErrorComponent } from './error/error.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {MatMenuModule} from "@angular/material/menu";


@NgModule({
  declarations: [
    MainComponent,
    NavbarComponent,
    FooterComponent,
    ErrorComponent
  ],
    imports: [
        CommonModule,
        SharedModule,
        RouterModule,
        MatIconModule,
        MatToolbarModule,
        MatButtonModule,
        MatSidenavModule,
        MatListModule,
        MatMenuModule
    ]
})
export class LayoutModule { }
