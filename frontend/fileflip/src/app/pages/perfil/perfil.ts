import { Component, inject, signal } from '@angular/core';
import { ArquivoService, Arquivo } from '../../service/arquivo';
import { Usuario, UsuarioService } from '../../service/usuario';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';
import { Pesquisa } from '../../components/pesquisa/pesquisa';
import { AvatarModule } from 'primeng/avatar';
import { Paginator } from '../../components/paginator/paginator';

@Component({
    selector: 'app-perfil',
    templateUrl: './perfil.html',
    standalone: true,
    imports: [
        ButtonModule, 
        CommonModule, 
        CardModule, 
        SkeletonModule,
        Pesquisa,
        Paginator,
        AvatarModule
    ],
    providers: [ArquivoService]
})
export class Perfil {
    usuarioService = inject(UsuarioService);
    arquivoService = inject(ArquivoService);
    
    perfil = signal<Usuario | null>(null);
    arquivos = signal<Arquivo[]>([]);
    loading = signal<boolean>(true);

    constructor() {
        // Aguarda o próximo ciclo de detecção de mudanças
        setTimeout(() => this.carregarPerfil(), 0);
    }

    carregarPerfil(): void {
        const userId = sessionStorage.getItem('userId');
        
        if (!userId) {
            console.error('UserId não encontrado no sessionStorage');
            this.loading.set(false);
            return;
        }

        console.log('Carregando perfil para userId:', userId);

        this.usuarioService.getPerfil(userId).subscribe({
            next: (data) => {
                console.log('Perfil carregado:', data);
                this.perfil.set({
                    id: data.id,
                    nome: data.nome,
                    email: data.email,
                    foto_perfil: data.foto_perfil,
                    qtd_arquivos: data.qtd_arquivos
                });
                
                if (data.arquivos) {
                    this.arquivos.set(data.arquivos);
                }
                
                this.loading.set(false);
            },
            error: (err) => {
                console.error('Erro ao carregar perfil:', err);
                this.loading.set(false);
            }
        });
    }

    removerArquivo(arquivo: any) {
        this.arquivos.set(this.arquivos().filter(a => a !== arquivo));
    }
}