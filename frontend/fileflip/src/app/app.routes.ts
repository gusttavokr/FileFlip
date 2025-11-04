// app.routes.ts
import { Routes } from '@angular/router';
import { Registro } from './pages/registro/registro';
import { Index } from './pages/index/index';
import { Arquivos } from './pages/arquivos/arquivos';

export const routes: Routes = [
  { path: '', component: Index },        // rota raiz
  { path: 'registro', component: Registro },        // rota registro
  { path: 'meus-arquivos', component: Arquivos },  // rota /arquivos
];
