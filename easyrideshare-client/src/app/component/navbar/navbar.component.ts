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
  routerSubscription?: Subscription;

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
    this.routerSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const url = event.urlAfterRedirects;
        console.log('Url', url);
        if (!url.startsWith('/sign-in')) {
          console.log('Updating query param');
          this.currentUrl = event.urlAfterRedirects;
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.authUserSubscription?.unsubscribe();
    this.routerSubscription?.unsubscribe();
  }

  onLogout(): void {
    this.authService.logout();
  }
}
