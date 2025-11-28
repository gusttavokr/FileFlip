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
            next: (data: any) => {
                console.log('Perfil carregado completo:', data);
                
                // O backend retorna {usuario: {...}, arquivos: [...]}
                const usuario = data.usuario || data;
                const arquivos = data.arquivos || [];
                
                console.log('Usuario extraído:', usuario);
                console.log('Arquivos extraídos:', arquivos);
                
                this.perfil.set({
                    id: usuario.id || usuario.userId,
                    nome: usuario.username || usuario.nome || 'Usuário',
                    email: usuario.email,
                    foto_perfil: usuario.foto_perfil || usuario.fotoPerfil,
                    qtd_arquivos: arquivos.length
                });
                
                this.arquivos.set(arquivos);
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

    formatBytes(bytes: number): string {
        if (bytes === 0) return '0 Bytes';
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
    }
}