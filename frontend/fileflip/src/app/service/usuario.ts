import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { JwtPayload, jwtDecode } from 'jwt-decode';
import { CadastroUsuario } from '../models/CadastroUsuario';
import { LoginUsuario } from '../models/LoginUsuario';
import { Login } from '../models/Login';

export interface Usuario {
  id: string;
  nome: string;
  email: string;
  foto_perfil?: string;
  qtd_arquivos: number;
  arquivos?: any[];
}

@Injectable({
  providedIn: 'root',
})

export class UsuarioService {
  private readonly httpClient = inject(HttpClient);

  criarUsuario(usuario: CadastroUsuario): Observable<any> {
    return this.httpClient.post('http://172.31.176.1:8000/gateway/auth/cadastro', usuario);
  }

  logar(usuario: LoginUsuario): Observable<Login> {
    return this.httpClient.post<Login>('http://172.31.176.1:8000/gateway/auth/login', usuario);
  }

  getPerfil(userId: string): Observable<Usuario> {
    return this.httpClient.get<Usuario>(`http://172.31.176.1:8000/gateway/auth/${userId}/perfil`);
  }

  isAuthenticated(): boolean {
    return this.tokenDeAutenticacao() !== null;
  }

  tokenDeAutenticacao(): string | null {
    return sessionStorage.getItem('token');
  }

  getUserId(): string | null {
    return sessionStorage.getItem('userId');
  }

  getExpiration(): number | null {
    const token = this.tokenDeAutenticacao();
    if (!token) {
      return null;
    }

    try {
      const decodedToken = jwtDecode<JwtPayload>(token);
      return decodedToken.exp ?decodedToken.exp * 1000 : null;
    } catch (error) {
      console.error('Erro ao decodificar o token JWT:', error);
      return null;
    }
  }

  tokenVencido(): boolean {
    const expiration = this.getExpiration();
    if (!expiration) {
      return true;
    }
    return Date.now() > expiration;
  }
  // private usuarios: Usuario[] = [
  //   {
  //     id: '1',
  //     nome: "Histórico acadêmico",
  //     email: 'gustavommilitaoo@gmail.com',
  //     foto_perfil: 'https://randomuser.me/api/portraits/men/5.jpg',
  //     qtd_arquivos: 5,
  //   },
  // ];

  // constructor() {}

  // getUsuarios(): Observable<Usuario[]> {
  //   return of(this.usuarios);
  // }
}
