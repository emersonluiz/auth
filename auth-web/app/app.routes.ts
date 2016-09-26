import { ModuleWithProviders }  from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component'
import { ClientComponent } from './client/client.component'

const APP_ROUTES: Routes = [
    {path: '', component: LoginComponent},
    {path: 'login', component: LoginComponent},
    {path: 'client', component: ClientComponent}
];

export const routing: ModuleWithProviders = RouterModule.forRoot(APP_ROUTES);

