import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PageModeService {
  maintainScreenHeight: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor() { }

  setMaintainScreenHeight(isMaintained: boolean) {
    this.maintainScreenHeight.next(isMaintained);
  }

  getMaintainScreenHeight() {
    return this.maintainScreenHeight;
  }
}
