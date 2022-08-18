import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, Subject } from 'rxjs';
import { AuthUserInfoResponse } from '../model/auth-response.model';
import { MessageResponse } from '../model/response.model';
import { ErrorService } from './error.service';
import { StorageService } from './storage.service';

export interface AuthMessage {
    success: boolean;
    message: string;
}

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private messageSource = new Subject<AuthMessage>();

    constructor(
        private http: HttpClient,
        private storageService: StorageService,
        private errorService: ErrorService
    ) {}

    login(username: string, password: string): void {
        this.http
            .post<AuthUserInfoResponse | MessageResponse>(`api/auth/sign_in`, {
                username: username,
                password: password,
            })
            .pipe(
                catchError(
                    this.errorService.handleError<MessageResponse>('login', {
                        success: false,
                        message: 'Unknown error',
                    })
                )
            )
            .subscribe((response) => {
                if (response.success) {
                    const userInfo = response as AuthUserInfoResponse;
                    this.storageService.saveUser({
                        id: userInfo.id,
                        username: userInfo.username,
                        roles: userInfo.roles,
                    });
                    this.messageSource.next({
                        success: true,
                        message: 'Login success',
                    });
                } else {
                    this.messageSource.next({
                        success: false,
                        message: (response as MessageResponse).message,
                    });
                }
            });
    }

    register(username: string, email: string, password: string): void {
        this.http
            .post<MessageResponse>(`api/auth/sign_up`, {
                username: username,
                password: password,
                email: email,
            })
            .pipe(
                catchError(
                    this.errorService.handleError<MessageResponse>('register', {
                        success: false,
                        message: 'Unknown error',
                    })
                )
            )
            .subscribe((response) => {
                this.messageSource.next({
                    success: response.success,
                    message: response.message,
                });
            });
    }

    logout(): void {
        this.storageService.clean();
        this.http
            .post<MessageResponse>(`api/auth/sign_out`, {})
            .pipe(
                catchError(
                    this.errorService.handleError<MessageResponse>('logout', {
                        success: false,
                        message: 'Unknown error',
                    })
                )
            )
            .subscribe();
    }

    getMessages(): Observable<AuthMessage> {
        return this.messageSource.asObservable();
    }
}
