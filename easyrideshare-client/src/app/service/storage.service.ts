import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../model/user.model';

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  private userSource = new BehaviorSubject<User | undefined>(undefined);

  constructor() {
    const previousUser = window.sessionStorage.getItem(USER_KEY);
    if (previousUser !== null) {
      this.userSource.next(JSON.parse(previousUser));
    }
  }

  clean(): void {
    window.sessionStorage.clear();
    this.userSource.next(undefined);
  }

  saveUser(user: User): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    this.userSource.next({ ...user });
  }

  getUser(): Observable<User | undefined> {
    return this.userSource.asObservable();
  }
}
