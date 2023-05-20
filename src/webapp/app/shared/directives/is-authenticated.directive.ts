import {Directive, ElementRef, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef} from '@angular/core';
import {AccountService} from "../../services/account.service";
import {Subscription} from "rxjs";

@Directive({
  selector: '[isAuthenticated]'
})
export class IsAuthenticatedDirective implements OnInit, OnDestroy {
  private mustBeAuthenticated: boolean = false;
  private observable?: Subscription;

  constructor(private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef,
              private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.observable = this.accountService.currentAccount$.subscribe(_ => {
      this.updateView();
    });
  }

  ngOnDestroy(): void {
    this.observable?.unsubscribe();
  }

  @Input()
  set isAuthenticated(mustBeAuthenticated: boolean) {
    this.mustBeAuthenticated = mustBeAuthenticated;
    this.updateView();
  }

  private updateView() {
    if (this.mustBeAuthenticated == this.accountService.isAuthenticated()) {
      this.viewContainer.clear();
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
