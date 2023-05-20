import {Directive, Input, OnDestroy, TemplateRef, ViewContainerRef} from '@angular/core';
import {Subscription} from "rxjs";
import {AccountService} from "../../services/account.service";

@Directive({
  selector: '[hasAnyRoles]'
})
export class HasAnyRolesDirective implements OnDestroy {
  private observable?: Subscription;

  constructor(private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef,
              private accountService: AccountService) { }

  ngOnDestroy(): void {
    this.observable?.unsubscribe();
  }

  @Input()
  set hasAnyRoles(roles: string[]) {
    if(this.observable) return;
    roles = roles.map(x => "ROLE_" + x);
    this.observable = this.accountService.currentAccount$.subscribe(x => {
      if (this.accountService.isAuthenticated() &&
        this.accountService.currentAccount?.authorities.some(r => roles.includes(r))) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainer.clear();
      }
    });
  }
}
