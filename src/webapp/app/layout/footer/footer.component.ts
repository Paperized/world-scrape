import {Component} from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  langs = [{ langText: 'EN', langKey: 'en'}, { langText: 'IT', langKey: 'it'}];

  constructor(private translateService: TranslateService) {
  }


  changeLang(langKey: string) {
    this.translateService.use(langKey);
  }
}
