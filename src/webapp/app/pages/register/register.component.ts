import {Component} from '@angular/core';
import {AbstractControl, FormBuilder, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {Login} from "../../models/Login";
import {Register} from "../../models/Register";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  errorCodeResult?: string;

  registerForm = this.formBuilder.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(16)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6), this.validateMatchingPassword(true)]],
    confirmPassword: ['', [Validators.required, this.validateMatchingPassword(false)]]
  });

  constructor(private formBuilder: FormBuilder, private router: Router, private authService: AuthService) {
  }

  onSubmit() {
    const register = new Register(
      this.registerForm.value.username!,
      this.registerForm.value.email!,
      this.registerForm.value.password!
    );

    this.authService.register(register)
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

  validateMatchingPassword(isOriginal: boolean) {
    return (control: AbstractControl) => {
      if (!control.parent) return null;
      let otherControl = isOriginal ? control.parent.get('confirmPassword') : control.parent.get('password');
      const res = otherControl?.value === control.value ? null : {'passwordMismatch': true};
      if (isOriginal && res) {
        otherControl?.setErrors(res);
        return null;
      }

      return res;
    }
  }
}
