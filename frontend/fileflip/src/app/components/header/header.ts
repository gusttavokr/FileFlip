import { Component, OnInit } from '@angular/core';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { Ripple } from 'primeng/ripple';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-header',
  imports: [
    MenubarModule, 
    BadgeModule, 
    AvatarModule, 
    InputTextModule, 
    Ripple, 
    CommonModule,
  ],
  templateUrl: './header.html',
})
export class Header implements OnInit {
  items: MenuItem[] | undefined;

  ngOnInit() {
    this.items = [
    { label: 'Página inicial', icon: 'pi pi-home', routerLink: '/' },
    { label: 'Edição em lotes', routerLink: '/edicao' },
    { label: 'Meus arquivos', routerLink: '/arquivos' },
    { label: 'Configurações', routerLink: '/configuracoes' },
  ];
  }

}
