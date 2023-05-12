import {ChangeDetectorRef, Component, ViewEncapsulation} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Login} from "../../models/Login";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {catchError} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  errorCodeResult?: string;

  loginForm = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  constructor(private formBuilder: FormBuilder, private router: Router,
              private authService: AuthService) { }

  onSubmit() {
    this.errorCodeResult = undefined;
    this.authService.login(new Login(this.loginForm.value.email!, this.loginForm.value.password!))
      .subscribe({
        next: _ => this.router.navigate(['/']),
        error: err => this.errorCodeResult = err.error.errors[0].errorCode
      });
  }

  getFirstErrorMessage(control: AbstractControl) {
    const errors = control.errors;
    if (errors) {
      const firstErrorKey = Object.keys(errors)[0];
      return "errors.form." + firstErrorKey;
    }

    return null;
  }
}
