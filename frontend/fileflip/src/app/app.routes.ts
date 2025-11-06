// app.routes.ts
import { Routes } from '@angular/router';
import { Registro } from './pages/registro/registro';
import { Index } from './pages/index/index';
import { Arquivos } from './pages/arquivos/arquivos';
import { Login } from './pages/login/login';

export const routes: Routes = [
  { path: '', component: Index },        
  { path: 'registro', component: Registro },        
  { path: 'meus-arquivos', component: Arquivos },  
  { path: 'login', component: Login },         
];
