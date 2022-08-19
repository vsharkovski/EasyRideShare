import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { iif, of, Subscription, switchMap, take, timer } from 'rxjs';
import { RouteTrackingService } from 'src/app/service/route-tracking.service';
import { AuthMessage, AuthService } from '../../service/auth.service';
import { StorageService } from '../../service/storage.service';

@Component({
  selector: 'ers-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, OnDestroy {
  form = this.formBuilder.group({
    username: [
      '',
      [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(20),
        Validators.pattern('^[A-Za-z\\d]+$'),
      ],
    ],
    password: ['', [Validators.required, Validators.maxLength(40)]],
  });
  formSubmitted: boolean = false;
  authMessage?: AuthMessage;
  readonly redirectWaitTime: number = 3;
  redirectElapsedTime?: number = undefined;

  authMessageSubscription?: Subscription;
  redirectSubscription?: Subscription;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private storageService: StorageService,
    private routeTrackingService: RouteTrackingService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // If already logged in when entering the page, redirect to home
    this.storageService
      .getUser()
      .pipe(take(1))
      .subscribe((user) => {
        if (user !== undefined && !this.formSubmitted) {
          this.router.navigate(['/home']);
        }
      });

    this.authMessageSubscription = this.authService
      .getMessages()
      .subscribe((message) => {
        this.authMessage = message;
      });

    // When user logs in, start timer and redirect after time has passed
    this.redirectSubscription = this.storageService
      .getUser()
      .pipe(
        switchMap((user) =>
          iif(() => user !== undefined, timer(0, 1000), of(undefined))
        ),
        take(this.redirectWaitTime + 1)
      )
      .subscribe({
        next: (time) => {
          this.redirectElapsedTime = time;
        },
        complete: () => {
          if (this.redirectElapsedTime !== undefined) {
            this.routeTrackingService.navigateToPreviousUrl();
          }
        },
      });
  }

  ngOnDestroy(): void {
    this.authMessageSubscription?.unsubscribe();
    this.redirectSubscription?.unsubscribe();
  }

  onSubmit(): void {
    if (this.form.invalid) {
      return;
    }
    const { username, password } = this.form.value;
    this.authService.login(username!, password!);
    this.formSubmitted = true;
  }

  get usernameField(): AbstractControl {
    return this.form.get('username')!;
  }

  get passwordField(): AbstractControl {
    return this.form.get('password')!;
  }
}
