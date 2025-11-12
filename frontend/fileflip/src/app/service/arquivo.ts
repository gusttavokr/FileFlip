import { Injectable } from '@angular/core';

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
      id: '5',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    },
    {
      id: '4',
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
    {
      id: '2',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    },
    {
      id: '1',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    },
    {
      id: '5',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    },
    {
      id: '4',
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
    {
      id: '2',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    },
    {
      id: '1',
      nome: "Histórico acadêmico",
      tipo: 'PDF',
      tamanho: 100,
    }
  ];

  getArquivos(): Arquivo[] {
    return this.arquivos;
  }
}
