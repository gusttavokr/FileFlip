import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'app-registro',
    templateUrl: './registro.html',
    standalone: true,
    imports: [
        FormsModule, 
        InputGroupModule, 
        InputGroupAddonModule, 
        InputTextModule, 
        SelectModule, 
        InputNumberModule,
        PasswordModule,
        ButtonModule
    ]
})
export class Registro {
    username: string | undefined;

    email: string | undefined;

    senha!: string;
    confirmarSenha!: string;
}