import { Routes } from '@angular/router';
import { ListPage } from './features/multimedia/pages/list/list.page';
import { RegisterPage } from './features/multimedia/pages/register/register.page';


export const routes: Routes = [
  { path: '', redirectTo: 'register', pathMatch: 'full' },
  { path: 'register', component: RegisterPage },
  { path: 'list', component: ListPage },
];