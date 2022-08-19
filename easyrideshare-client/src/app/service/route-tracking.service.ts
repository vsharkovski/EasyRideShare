import { Injectable } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class RouteTrackingService {
  private _previousUrl?: string;
  private _currentUrl?: string;

  public get previousUrl(): string | undefined {
    return this._previousUrl;
  }

  public get currentUrl(): string | undefined {
    return this._currentUrl;
  }

  constructor(private router: Router) {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this._previousUrl = this.currentUrl;
        this._currentUrl = event.urlAfterRedirects;
        console.log('prev', this.previousUrl, 'curr', this.currentUrl);
      }
    });
  }

  navigateToPreviousUrl(): void {
    const url = this._previousUrl;
    const newUrl = url !== undefined ? [url] : ['/home'];
    console.log('Redirecting', newUrl);
    this.router.navigate(newUrl);
  }
}
