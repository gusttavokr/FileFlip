import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

export interface Arquivo {
  id: string;
  nome: string;
  tipo: string;
  tamanho: number;
}

@Injectable({
  providedIn: 'root',
})
export class ArquivoService {
  private arquivos: Arquivo[] = [
    {
      id: '1',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    },
    {
      id: '2',
      nome: "Declaração de vínculo",
      tipo: 'PDF',
      tamanho: 250,
    },
    {
      id: '3',
      nome: "Prova de Interfaces Ricas",
      tipo: 'DOCX',
      tamanho: 1000,
    },
  ];

  constructor() {}

  getArquivos(): Observable<Arquivo[]> {
    return of(this.arquivos);
  }
}
