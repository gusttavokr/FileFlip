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
    // Usuario logado
    perfil = signal<Usuario>({
        id: '1',
        nome: 'Eduardo Braulio',
        email: 'eduardo.braulio@gmail.com',
        foto_perfil: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQWO2tYCqw50LbSI7diQb0fhHCfEEpeTEtYrA&s',
        qtd_arquivos: 10,
    });
    arquivoService = inject(ArquivoService);
    arquivos = signal<Arquivo[]>(this.arquivoService.getArquivos());

    removerArquivo(arquivo: any) {
        this.arquivos.set(this.arquivos().filter(a => a !== arquivo));
    }
}