import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { InputTextModule } from 'primeng/inputtext';
import { SelectModule } from 'primeng/select';
import { InputNumberModule } from 'primeng/inputnumber';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

import { UsuarioService } from '../../service/usuario';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CadastroUsuario } from '../../models/CadastroUsuario';

@Component({
    selector: 'app-registro',
    templateUrl: './registro.html',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule, 
        ReactiveFormsModule,
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
export class Registro {
    readonly usuarioService = inject(UsuarioService);
    private router = inject(Router);

    cadastroDados: FormGroup = new FormGroup({
        username: new FormControl('', [Validators.required]),
        email: new FormControl('', [Validators.required, Validators.email]),
        password: new FormControl('', [Validators.required, Validators.minLength(6)]),
        confirmPassword: new FormControl('', [Validators.required])
    });

    onSubmit() {
        if (this.cadastroDados.invalid) {
            this.cadastroDados.markAllAsTouched();
            return;
        }

        console.log('Enviando cadastro:', this.cadastroDados.value);

        this.usuarioService.criarUsuario(this.cadastroDados.value as CadastroUsuario).subscribe({
            next: (response) => {
                console.log('Cadastro bem-sucedido:', response);
                alert('Cadastro realizado com sucesso! Faça login para continuar.');
                this.router.navigate(['/login']);
            },
            error: (err) => {
                console.error('Erro ao cadastrar usuário:', err);
                alert('Erro ao cadastrar: ' + (err.error?.message || 'Erro desconhecido'));
            }
        });
    
    }
}