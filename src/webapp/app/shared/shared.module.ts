import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TranslateModule} from "@ngx-translate/core";
import {ErrorTranslatorDirective} from "./directives/error-translator.directive";

@NgModule({
  imports: [
    CommonModule
  ],
  exports: [
    TranslateModule,
    ErrorTranslatorDirective
  ],
  declarations: [
    ErrorTranslatorDirective
  ]
})
export class SharedModule { }
