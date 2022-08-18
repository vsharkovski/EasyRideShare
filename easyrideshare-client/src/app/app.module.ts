import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { PostListComponent } from './post-list/post-list.component';
import { HomeFeedComponent } from './home-feed/home-feed.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { httpInterceptorProviders } from './http-interceptor';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        PostListComponent,
        HomeFeedComponent,
        NotFoundComponent,
    ],
    imports: [BrowserModule, AppRoutingModule, NgbModule, HttpClientModule],
    providers: [httpInterceptorProviders],
    bootstrap: [AppComponent],
})
export class AppModule {}
