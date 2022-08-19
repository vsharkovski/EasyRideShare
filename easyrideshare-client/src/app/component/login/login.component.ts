import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { iif, interval, of, Subscription, switchMap, take, timer } from 'rxjs';
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

    authMessage?: AuthMessage;
    readonly redirectWaitTime: number = 5;
    redirectElapsedTime?: number = undefined;

    redirectSubscription?: Subscription;
    authMessageSubscription?: Subscription;

    constructor(
        private formBuilder: FormBuilder,
        private authService: AuthService,
        private storageService: StorageService,
        private route: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.authMessageSubscription = this.authService
            .getMessages()
            .subscribe((message) => {
                this.authMessage = message;
            });
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
                    console.log('Got time', time);
                },
                complete: () => {
                    if (this.redirectElapsedTime === undefined) {
                        return;
                    }
                    const redirect = this.route.snapshot.queryParams['r'];
                    const newRoute =
                        redirect !== undefined ? [redirect] : ['/home'];
                    console.log('Login redirect', newRoute);
                    this.router.navigate(newRoute);
                },
            });
    }

    ngOnDestroy(): void {
        this.redirectSubscription?.unsubscribe();
        this.authMessageSubscription?.unsubscribe();
    }

    onSubmit(): void {
        if (this.form.invalid) {
            return;
        }
        const { username, password } = this.form.value;
        this.authService.login(username as string, password as string);
    }

    get usernameField(): AbstractControl {
        return this.form.get('username')!;
    }

    get passwordField(): AbstractControl {
        return this.form.get('password')!;
    }
}
