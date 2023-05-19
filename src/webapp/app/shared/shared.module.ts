import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TranslateModule} from "@ngx-translate/core";
import {ErrorTranslatorDirective} from "./directives/error-translator.directive";
import {YesNoDialog} from "./yes-no-dialog/yes-no-dialog.component";
import {MatDialogModule} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";

@NgModule({
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ],
  exports: [
    TranslateModule,
    ErrorTranslatorDirective,
    YesNoDialog
  ],
  declarations: [
    ErrorTranslatorDirective,
    YesNoDialog
  ]
})
export class SharedModule { }
