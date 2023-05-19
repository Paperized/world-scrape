import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HomeComponent} from "./home/home.component";
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {SharedModule} from "../shared/shared.module";
import {RouterModule} from "@angular/router";
import {PAGES_ROUTES} from "./pages.routes";
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {ScraperExecutionComponent} from "./scraper-execution/scraper-execution.component";
import {MatMenuModule} from "@angular/material/menu";
import {MatSelectModule} from "@angular/material/select";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatListModule} from "@angular/material/list";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {ScraperCreationComponent} from "./scraper-creation/scraper-creation.component";
import {MatDialogModule} from "@angular/material/dialog";

@NgModule({
  declarations: [
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    ScraperExecutionComponent,
    ScraperCreationComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    RouterModule.forChild(PAGES_ROUTES),
    MatCardModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatMenuModule,
    MatSelectModule,
    MatAutocompleteModule,
    MatListModule,
    MatSnackBarModule,
    MatDialogModule
  ]
})
export class PagesModule {
}
