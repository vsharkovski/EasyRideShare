import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeFeedComponent } from './component/home-feed/home-feed.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { LoginComponent } from './component/login/login.component';

const routes: Routes = [
    { path: 'home', component: HomeFeedComponent },
    { path: 'sign-in', component: LoginComponent },
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
