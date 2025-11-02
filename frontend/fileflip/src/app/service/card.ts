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
      titulo: 'AAAAAA',
      legenda: 'AAAAAAAAA',
    },
    {
      icon: "",
      titulo: 'BBBBBBB',
      legenda: 'BBBBBBBBBBBBBBB',
    },
    {
      icon: "",
      titulo: 'AAAAAA',
      legenda: 'AAAAAAAAA',
    },
    {
      icon: "",
      titulo: 'BBBBBBB',
      legenda: 'BBBBBBBBBBBBBBB',
    },
    
  ];

  constructor() {}

  getCards(): Observable<CardSimples1[]> {
    return of(this.cards);
  }
}
