import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

export interface CardSimples1 {
  icon: string;
  titulo: string;
  legenda: string;
}

@Injectable({
  providedIn: 'root',
})
export class CardSimplesService {
  private cards: CardSimples1[] = [
    {
      icon: "",
      titulo: 'Prático e Eficiente',
      legenda: 'Converta seus arquivos em poucos segundos, sem complicações.',
    },
    {
      icon: "",
      titulo: 'Seguro e Confiável',
      legenda: 'Proteja seus dados com criptografia de ponta a ponta.',
    },
    {
      icon: "",
      titulo: 'Múltiplos Formatos',
      legenda: 'Vários tipos de arquivos compatíveis, tudo em um único lugar.',
    },
    {
      icon: "",
      titulo: 'Qualquer Lugar',
      legenda: 'Use diretamente do navegador, sem precisar instalar nada.',
    },
    
  ];

  constructor() {}

  getCards(): Observable<CardSimples1[]> {
    return of(this.cards);
  }
}
