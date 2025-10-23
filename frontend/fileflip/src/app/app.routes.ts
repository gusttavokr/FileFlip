// app.routes.ts
import { Routes } from '@angular/router';
import { Registro } from './pages/registro/registro';
import { Arquivos } from './pages/arquivos/arquivos';

export const routes: Routes = [
  { path: '', component: Registro },        // rota raiz
  { path: 'meus-arquivos', component: Arquivos },  // rota /arquivos
];
