import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ConversaoRequest } from '../models/ConversaoRequest';
import { ConversaoResponse } from '../models/ConversaoResponse';

export interface Arquivo {
  arquivo_id: string;
  name: string;
  tamanhoArquivo: number;
  possuiFoto: boolean;
  usuario_id: string;
  url_download: string;
}

@Injectable({
  providedIn: 'root',
})
export class ArquivoService {
  private readonly httpClient = inject(HttpClient);
  
  // private arquivos: Arquivo[] = [
  //   {
  //     id: '5',
  //     nome: "Histórico acadêmico",
  //     tipo: 'PDF',
  //     tamanho: 100,
  //   },
  //   {
  //     id: '4',
  //     nome: "Declaração de vínculo",
  //     tipo: 'PDF',
  //     tamanho: 250,
  //   },
  //   {
  //     id: '3',
  //     nome: "Prova de Interfaces Ricas",
  //     tipo: 'DOCX',
  //     tamanho: 1000,
  //   },
  //   {
  //     id: '2',
  //     nome: "Histórico acadêmico",
  //     tipo: 'PDF',
  //     tamanho: 100,
  //   },
  //   {
  //     id: '1',
  //     nome: "Histórico acadêmico",
  //     tipo: 'PDF',
  //     tamanho: 100,
  //   },
  //   {
  //     id: '5',
  //     nome: "Histórico acadêmico",
  //     tipo: 'PDF',
  //     tamanho: 100,
  //   },
  //   {
  //     id: '4',
  //     nome: "Declaração de vínculo",
  //     tipo: 'PDF',
  //     tamanho: 250,
  //   },
  //   {
  //     id: '3',
  //     nome: "Prova de Interfaces Ricas",
  //     tipo: 'DOCX',
  //     tamanho: 1000,
  //   },
  //   {
  //     id: '2',
  //     nome: "Histórico acadêmico",
  //     tipo: 'PDF',
  //     tamanho: 100,
  //   },
  //   {
  //     id: '1',
  //     nome: "Histórico acadêmico",
  //     tipo: 'PDF',
  //     tamanho: 100,
  //   }
  // ];

  // getArquivos(): Arquivo[] {
  //   return this.arquivos;
  // }

  converter(arquivo: File, novoTipo: string): Observable<ConversaoResponse> {
    const formData = new FormData();
    formData.append('arquivo', arquivo);
    formData.append('novoTipo', novoTipo);

    return this.httpClient.post<ConversaoResponse>('http://localhost:8000/gateway/api/v1/converter/', formData);
  }
}
