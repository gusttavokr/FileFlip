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
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../service/usuario';
import { LoginUsuario } from '../../models/LoginUsuario';
import { AuthStateService } from '../../service/auth-state';

@Component({
    selector: 'app-login',
    templateUrl: './login.html',
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
export class Login {
//     constructor(private router: Router) {}
//     username: string | undefined;

//     email: string | undefined;

//     senha!: string;
//     confirmarSenha!: string;

//     onCadastroSucesso() {
//         this.router.navigate(['/meus-arquivos']);
//   }

    readonly usuarioService = inject(UsuarioService);
    readonly authState = inject(AuthStateService);
    private router = inject(Router);

    loginDados: FormGroup = new FormGroup({
        email: new FormControl('', [Validators.required, Validators.email]),
        password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    });

    onSubmit() {
        if (this.loginDados.invalid) {
            this.loginDados.markAllAsTouched();
            return;
        }

        console.log('Enviando login:', this.loginDados.value);

        this.usuarioService.logar(this.loginDados.value as LoginUsuario).subscribe({
            next: (response) => {
                console.log('Login bem-sucedido:', response);
                if (response.token) {
                    const userIdString = typeof response.userId === 'string' 
                        ? response.userId 
                        : String(response.userId);
                    
                    this.authState.setAuth(response.token, response.username, userIdString);
                    console.log('Token salvo:', response.token);
                    console.log('UserId salvo:', userIdString);
                }
                this.router.navigate(['/']);
            },
            error: (err) => {
                console.error('Erro ao fazer login:', err);
                alert('Erro ao fazer login: ' + (err.error?.message || 'Erro desconhecido'));
            }
        });
    
    }    
}