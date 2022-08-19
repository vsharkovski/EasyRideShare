import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription, take } from 'rxjs';
import { AuthMessage, AuthService } from 'src/app/service/auth.service';
import { StorageService } from 'src/app/service/storage.service';
import { passwordConfirmValidator } from 'src/app/shared/password-confirm.validator';

@Component({
  selector: 'ers-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  form = this.formBuilder.group(
    {
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
          Validators.pattern('^[A-Za-z\\d]+$'),
        ],
      ],
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(50)],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.maxLength(40),
        ],
      ],
      passwordConfirm: [''],
    },
    { validators: passwordConfirmValidator }
  );

  authMessage?: AuthMessage;

  authMessageSubscription?: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private storageService: StorageService,

    private router: Router
  ) {}

  ngOnInit(): void {
    // If already logged in when entering the page, redirect to home
    this.storageService
      .getUser()
      .pipe(take(1))
      .subscribe((user) => {
        if (user !== undefined) {
          this.router.navigate(['/home']);
        }
      });

    this.authMessageSubscription = this.authService
      .getMessages()
      .subscribe((message) => (this.authMessage = message));
  }

  ngOnDestroy(): void {
    this.authMessageSubscription?.unsubscribe();
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }
    const { username, email, password, passwordConfirm } = this.form.value;
    this.authService.register(username!, email!, password!);
  }

  get usernameField(): AbstractControl {
    return this.form.get('username')!;
  }

  get emailField(): AbstractControl {
    return this.form.get('email')!;
  }

  get passwordField(): AbstractControl {
    return this.form.get('password')!;
  }

  get passwordConfirmField(): AbstractControl {
    return this.form.get('passwordConfirm')!;
  }
}
