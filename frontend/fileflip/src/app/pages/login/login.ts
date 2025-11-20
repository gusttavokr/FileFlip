import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.html',
    standalone: true,
    imports: [
        FormsModule, 
        InputGroupModule, 
        InputGroupAddonModule, 
        InputTextModule, 
        SelectModule, 
        InputNumberModule,
        PasswordModule,
        ButtonModule,
        RouterModule
    ]
})
export class Login {
    constructor(private router: Router) {}
    username: string | undefined;

    email: string | undefined;

    senha!: string;
    confirmarSenha!: string;

    onCadastroSucesso() {
        this.router.navigate(['/meus-arquivos']);
  }
}