import {Directive, Input, OnDestroy, TemplateRef, ViewContainerRef} from '@angular/core';
import {AccountService} from "../../services/account.service";
import {Subscription} from "rxjs";

@Directive({
  selector: '[isAuthenticated]'
})
export class IsAuthenticatedDirective implements OnDestroy {
  private observable?: Subscription;

  constructor(private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef,
              private accountService: AccountService) { }

  ngOnDestroy(): void {
    this.observable?.unsubscribe();
  }

  @Input()
  set isAuthenticated(mustBeAuthenticated: boolean) {
    this.observable?.unsubscribe();
    this.observable = this.accountService.currentAccount$.subscribe(x => {
      if (mustBeAuthenticated == this.accountService.isAuthenticated()) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    });
  }
}
