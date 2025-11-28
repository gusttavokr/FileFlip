import { Component } from '@angular/core';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { Ripple } from 'primeng/ripple';
import { Router, RouterModule } from '@angular/router';
import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';
import { AuthStateService } from '../../service/auth-state';

@Component({
  selector: 'app-header',
  imports: [
    MenubarModule, 
    BadgeModule, 
    AvatarModule, 
    InputTextModule, 
    CommonModule,
    RouterModule,
  ],
  templateUrl: './header.html',
})
export class Header {

  items : MenuItem[] = [
    // { label: 'Página inicial', icon: 'pi pi-home', routerLink: '/' },
    { label: 'Suporte', icon:'pi pi-phone' ,routerLink: '/suporte' },
    { label: 'Configurações', icon:'pi pi-cog' , routerLink: '/configuracoes' },
  ];

  constructor(
    private router: Router,
    public authState: AuthStateService
  ) {}

  get isAuthenticated() {
    return this.authState.isAuthenticated();
  }

  get userName() {
    return this.authState.userName();
  }

  logout(): void {
    this.authState.clearAuth();
    this.router.navigate(['/login']);
  }

}
