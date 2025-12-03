// app.routes.ts
import { Routes } from '@angular/router';
import { Registro } from './pages/registro/registro';
import { Index } from './pages/index/index';
import { Perfil } from './pages/perfil/perfil';
import { Login } from './pages/login/login';
import { guardAuth } from './service/usuario.guard';

export const routes: Routes = [
  { path: '', component: Index },        
  { path: 'registro', component: Registro },        
  { path: 'perfil', component: Perfil, canActivate: [guardAuth] },  
  { path: 'login', component: Login },         
];
