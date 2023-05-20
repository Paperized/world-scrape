import {Component, OnDestroy, OnInit} from '@angular/core';
import {jsonMockedResult} from "./mocked-values";
import {ScraperService} from "../../services/scraper.service";
import {PageModeService} from "../../services/page-mode.service";
import {ScraperConfiguration} from "../../models/ScraperConfiguration";
import {FormControl} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";
import {ScraperConfigParamType, ScraperConfigurationParam} from "../../models/ScraperConfigurationParam";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-scraper-execution',
  templateUrl: './scraper-execution.component.html',
  styleUrls: ['./scraper-execution.component.scss']
})
export class ScraperExecutionComponent implements OnInit, OnDestroy {
  configurations: ScraperConfiguration[] = [];
  configAutocompleteControl = new FormControl<string | ScraperConfiguration>('');
  filteredOptions: Observable<ScraperConfiguration[]> | undefined;

  currentlySelectedIdConfig?: number;
  url: string = '';
  selectedConfigUrl: string = '';
  scraperParams: ScraperConfigurationParam[] = [];
  counterParamsAdded = 0;

  isLoading = false;
  errorCodeResult?: string;
  scrapeResult: any = JSON.parse(jsonMockedResult);

  constructor(private scraperService: ScraperService, private pageMode: PageModeService,
              private snackbarService: MatSnackBar) {
  }

  ngOnInit(): void {
    this.pageMode.setMaintainScreenHeight(true);
    this.scraperService.getAllConfigurations().subscribe({
      next: cfgs => {
        this.configurations = cfgs;
        this.configAutocompleteControl.setValue('');
      }
    });
    this.filteredOptions = this.configAutocompleteControl.valueChanges.pipe(
      startWith(''),
      map(value => {
        const name = typeof value === 'string' ? value : value?.name;
        return name ? this._filter(name as string) : this.configurations.slice();
      }),
    );
  }

  ngOnDestroy(): void {
    this.pageMode.setMaintainScreenHeight(false);
  }

  onConfigSelectedChanged(value: any) {
    if(value == null || typeof value === 'string') return;
    if(value.id != this.currentlySelectedIdConfig) {
      this.currentlySelectedIdConfig = value.id;
      this.url = value.urlStyle;
      this.selectedConfigUrl = value.configurationUrl;
      this.scraperParams = [...value.params];
    }
  }

  noPropagation(event: MouseEvent) {
    event.stopPropagation();
  }

  displayFn(config: ScraperConfiguration): string {
    return config && config.name ? `${config.name} (${config.userUsername})` : '';
  }

  private _filter(name: string): ScraperConfiguration[] {
    const filterValue = name.toLowerCase();

    return this.configurations.filter(cfg => cfg.name.toLowerCase().includes(filterValue));
  }

  selectType(i: number, type: ScraperConfigParamType) {
    const param = this.scraperParams[i];
    if (param.type == type) return;
    param.type = type;

    switch (type) {
      case 'text':
        param.value = `${param.value}`;
        break;
      case 'number':
        param.value = +param.value!
        if (isNaN(<number>param.value) || param.value == Infinity) param.value = 0
        break;
      case 'bool':
        param.value = Boolean(param.value).valueOf()
        break;
      default:
        param.type = 'text';
        param.value = '';
        break;
    }
  }

  addParam() {
    this.scraperParams.push({name: `param${this.counterParamsAdded++}`, value: '', type: 'text', id: null, fileConfigId: null});
  }

  removeParam(index: number) {
    this.scraperParams.splice(index, 1);
  }

  copyResult() {
    let text = "";
    navigator.clipboard.writeText(this.scrapeResult)
      .then(() => text = "Result copied to clipboard!")
      .catch(() => text = "Something went wrong!")
      .finally(() => this.snackbarService.open(text, undefined,
                                    {verticalPosition: "top", duration: 1000}));
  }

  sendRequest() {
    const cleanParams = this.scraperParams.filter(x => x.name != '')
      .reduce((result: any, obj) => {
        result[obj.name] = obj.value;
        return result;
      }, {});

    this.errorCodeResult = undefined;
    this.isLoading = true;
    this.scraperService.runScraper(this.url, this.selectedConfigUrl, cleanParams).subscribe({
      next: res => {
        this.scrapeResult = res
      },
      error: err => this.errorCodeResult = err.error.errors[0].errorCode
    }).add(() => this.isLoading = false);
  }

  protected readonly JSON = JSON;
}
