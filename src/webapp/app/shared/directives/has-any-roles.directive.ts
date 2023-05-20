import {Directive, ElementRef, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef} from '@angular/core';
import {Subscription} from "rxjs";
import {AccountService} from "../../services/account.service";

@Directive({
  selector: '[hasAnyRoles]'
})
export class HasAnyRolesDirective implements OnInit, OnDestroy {
  private roles: string[] = [];
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
  set hasAnyRoles(roles: string[]) {
    this.roles = roles.map(x => "ROLE_" + x);
    this.updateView();
  }

  private updateView() {
    if (this.accountService.isAuthenticated() &&
      this.accountService.currentAccount?.authorities.some(r => this.roles.includes(r))) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
