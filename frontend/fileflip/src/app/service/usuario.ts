import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

export interface Usuario {
  id: string;
  nome: string;
  email: string;
  foto_perfil: string;
  qtd_arquivos: number;
}

@Injectable({
  providedIn: 'root',
})

export class UsuarioService {
  private usuarios: Usuario[] = [
    {
      id: '1',
      nome: "Histórico acadêmico",
      email: 'gustavommilitaoo@gmail.com',
      foto_perfil: 'https://randomuser.me/api/portraits/men/5.jpg',
      qtd_arquivos: 5,
    },
  ];

  constructor() {}

  getUsuarios(): Observable<Usuario[]> {
    return of(this.usuarios);
  }
}
