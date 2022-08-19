import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeFeedComponent } from './component/home-feed/home-feed.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';

const routes: Routes = [
  { path: 'home', component: HomeFeedComponent },
  { path: 'sign-in', component: LoginComponent },
  { path: 'sign-up', component: RegisterComponent },
  { path: 'profile/:id', component: HomeFeedComponent },
  { path: 'not-found', component: NotFoundComponent },
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: '**', pathMatch: 'full', redirectTo: 'not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
