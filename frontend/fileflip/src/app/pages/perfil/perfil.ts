import { Component, signal } from '@angular/core';
import { ArquivoService, Arquivo } from '../../service/arquivo';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
    selector: 'app-perfil',
    templateUrl: './perfil.html',
    standalone: true,
    imports: [
        ButtonModule, 
        CommonModule, 
        CardModule, 
        SkeletonModule
    ],
    providers: [ArquivoService]
})
export class Perfil {
    arquivos = signal<Arquivo[]>([]);

    constructor(private arquivoService: ArquivoService) {}

    ngOnInit() {
        this.arquivoService.getArquivos().subscribe((data) => {
            this.arquivos.set(data);
        });
    }

    removerArquivo(arquivo: any) {
        this.arquivos.set(this.arquivos().filter(a => a !== arquivo));
    }
}