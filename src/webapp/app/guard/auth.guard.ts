import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {AccountService} from "../services/account.service";

export const authGuard = () => {
  const accountService = inject(AccountService);
  const router = inject(Router);

  if(!accountService.isAuthenticated()) {
    return router.parseUrl('login');
  }

  return true;
}
