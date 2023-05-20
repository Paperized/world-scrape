import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TranslateModule} from "@ngx-translate/core";
import {ErrorTranslatorDirective} from "./directives/error-translator.directive";
import {YesNoDialog} from "./yes-no-dialog/yes-no-dialog.component";
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import { IsAuthenticatedDirective } from './directives/is-authenticated.directive';
import { HasAnyRolesDirective } from './directives/has-any-roles.directive';

@NgModule({
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ],
  exports: [
    TranslateModule,
    ErrorTranslatorDirective,
    IsAuthenticatedDirective,
    HasAnyRolesDirective,
    YesNoDialog
  ],
  declarations: [
    ErrorTranslatorDirective,
    YesNoDialog,
    IsAuthenticatedDirective,
    HasAnyRolesDirective
  ]
})
export class SharedModule { }
