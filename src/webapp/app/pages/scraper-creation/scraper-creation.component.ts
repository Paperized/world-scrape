import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {jsonMockedResult} from "../scraper-execution/mocked-values";
import {ScraperConfiguration} from "../../models/ScraperConfiguration";
import {FormControl} from "@angular/forms";
import {filter, map, Observable, of, startWith, tap} from "rxjs";
import {ScraperService} from "../../services/scraper.service";
import {PageModeService} from "../../services/page-mode.service";
import {ScraperConfigParamType} from "../../models/ScraperConfigurationParam";
import {MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {YesNoDialog} from "../../shared/yes-no-dialog/yes-no-dialog.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-scraper-creation',
  templateUrl: './scraper-creation.component.html',
  styleUrls: ['./scraper-creation.component.scss']
})
export class ScraperCreationComponent implements OnInit, OnDestroy {
  configurations: ScraperConfiguration[] = [];
  isResettingAutocompleteControl = false;
  configAutocompleteControl = new FormControl<string | ScraperConfiguration>('');
  filteredOptions: Observable<ScraperConfiguration[]> | undefined;

  isLoading: boolean = false;
  errorCodeResult?: string;
  currentConfigurationText: string = jsonMockedResult;
  currentConfiguration: ScraperConfiguration = new ScraperConfiguration(null, "", "", "", []);
  counterParamsAdded: number = 0;

  constructor(private scraperService: ScraperService, private pageMode: PageModeService, public dialogService: MatDialog) {
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
      filter(x => !this.isResettingAutocompleteControl),
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
    if (this.isResettingAutocompleteControl) return;

    if (value == null || typeof value === 'string') {
      return;
    }

    if (value.id != this.currentConfiguration.id) {
      this.currentConfiguration = structuredClone(value);
      this.scraperService.getConfigurationText(this.currentConfiguration).subscribe({
        next: t => this.currentConfigurationText = t
      });
    }
  }

  resetConfigurationSelected() {
    this.isResettingAutocompleteControl = true;
    this.configAutocompleteControl.setValue('');
    this.currentConfiguration = new ScraperConfiguration(null, "", "", "", []);
    this.currentConfigurationText = "";
    this.filteredOptions = of([...this.configurations]);
    this.isResettingAutocompleteControl = false;
  }

  resetConfigurationSelectedWithModal($event: MouseEvent) {
    this.isResettingAutocompleteControl = true;
    $event.stopPropagation();
    this.configAutocompleteControl.setValue('');
    this.openMaintainDialog().afterClosed().subscribe(result => {
      if (result) {
        this.currentConfiguration.id = null;
        this.currentConfiguration.params.forEach(x => x.id = x.fileConfigId = null);
      } else {
        this.currentConfiguration = new ScraperConfiguration(null, "", "", "", []);
        this.currentConfigurationText = "";
      }

      this.filteredOptions = of([...this.configurations]);
      this.isResettingAutocompleteControl = false;
    });
  }

  openMaintainDialog() {
    return this.dialogService.open(YesNoDialog, {data: {
      message: "Would you like to maintain all the fields currently in use?",
      title: "Maintain data"
    }});
  }

  noPropagation(event: MouseEvent) {
    event.stopPropagation();
  }

  displayFn(config: ScraperConfiguration): string {
    return config && config.name ? config.name : '';
  }

  private _filter(name: string): ScraperConfiguration[] {
    const filterValue = name.toLowerCase();

    return this.configurations.filter(cfg => cfg.name.toLowerCase().includes(filterValue));
  }

  selectType(i: number, type: ScraperConfigParamType) {
    const param = this.currentConfiguration.params[i];
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
    this.currentConfiguration.params.push({
      name: `param${this.counterParamsAdded++}`, value: '',
      type: 'text', id: null, fileConfigId: this.currentConfiguration.id
    });
  }

  removeParam(index: number) {
    this.currentConfiguration.params.splice(index, 1);
  }

  sendRequest() {
    this.isLoading = true;
    this.scraperService.saveOrUpdateConfiguration(this.currentConfiguration, this.currentConfigurationText).subscribe({
      next: res => {
        const prevConfigIndex = this.configurations.findIndex(x => x.id == this.currentConfiguration.id);
        if (prevConfigIndex == -1)
          this.configurations.push(res);
        else
          this.configurations[prevConfigIndex] = res;

        this.currentConfiguration = res;
        this.configAutocompleteControl.setValue(res);
        this.errorCodeResult = undefined;
      },
      error: err => this.errorCodeResult = err.error.errors[0].errorCode
    }).add(() => this.isLoading = false);
  }

  openDeleteDialog() {
    return this.dialogService.open(YesNoDialog, {data: {
        message: "Do you really want to delete this configuration?",
        title: "Delete Alert!"
      }});
  }

  deleteConfiguration() {
    if (!this.currentConfiguration.id) return;
    const configId = this.currentConfiguration.id;
    this.openDeleteDialog().afterClosed().subscribe(result => {
      if (result) {
        this.isLoading = true;
        this.scraperService.deleteConfiguration(configId).subscribe({
          next: _ => this.onConfigurationDeleted(),
          error: err => this.errorCodeResult = err.error.errors[0].errorCode
        }).add(() => this.isLoading = false);
      }
    });
  }

  onConfigurationDeleted() {
    const index = this.configurations.findIndex(x => x.id == this.currentConfiguration.id);
    this.configurations.splice(index, 1);
    this.resetConfigurationSelected();
  }
}
