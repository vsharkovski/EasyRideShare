import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './component/navbar/navbar.component';
import { PostListComponent } from './component/post-list/post-list.component';
import { HomeFeedComponent } from './component/home-feed/home-feed.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { httpInterceptorProviders } from './interceptor/http-interceptor';
import { LoginComponent } from './component/login/login.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './component/register/register.component';
import { PostCardComponent } from './component/post-card/post-card.component';
import { TransportTypePipe } from './shared/transport-type.pipe';
@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    PostListComponent,
    HomeFeedComponent,
    NotFoundComponent,
    LoginComponent,
    RegisterComponent,
    PostCardComponent,
    TransportTypePipe,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent],
})
export class AppModule {}
