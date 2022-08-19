import { Component, OnDestroy, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { map, Subscription } from 'rxjs';
import { User } from 'src/app/model/user.model';
import { AuthService } from 'src/app/service/auth.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'ers-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  isNavbarCollapsed: boolean = true;
  authUser?: User;
  currentUrl: String = '';

  authUserSubscription?: Subscription;

  constructor(
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.authUserSubscription = this.storageService
      .getUser()
      .subscribe((user) => {
        this.authUser = user;
      });
  }

  ngOnDestroy(): void {
    this.authUserSubscription?.unsubscribe();
  }

  onLogout(): void {
    this.authService.logout();
  }
}
