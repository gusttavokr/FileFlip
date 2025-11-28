import { Component, inject } from '@angular/core';
import { FileUploadModule } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { CommonModule } from '@angular/common';
import { BadgeModule } from 'primeng/badge';
import { HttpClientModule } from '@angular/common/http';
import { ProgressBarModule } from 'primeng/progressbar';
import { ToastModule } from 'primeng/toast';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';

import { Message } from 'primeng/message';
import { Toast } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { MessageModule } from 'primeng/message';

import { CardSimples } from '../../components/card-simples/card-simples';
import { ArquivoService } from '../../service/arquivo';

interface Format {
    name: string;
}

@Component({
  selector: 'app-index',
  standalone: true,
  imports: [
    CommonModule, 
    FileUploadModule, 
    ButtonModule, 
    BadgeModule, 
    ProgressBarModule, 
    ToastModule, 
    HttpClientModule, 
    SelectModule, 
    FormsModule,
    SelectModule, 
    FormsModule,
    MessageModule,
    CardSimples

],
  templateUrl: './index.html',
  providers: [MessageService],
})
export class Index {
  files: any[] = [];

  messageService = inject(MessageService);
  arquivoService = inject(ArquivoService);

  formatos: Format[] | undefined;

    formatoSelecionado: Format | undefined;

    ngOnInit() {
        this.formatos = [
            { name: 'PDF'},
            { name: 'DOCX'},
            { name: 'PNG'},
            { name: 'JPEG'},
            { name: 'SVG'}
        ];
    }

  totalSize: number = 0; // em bytes

  totalSizePercent: number = 0;

  // fallback local para unidades de tamanho — usado por formatSize
  private fileSizeTypes: string[] = ['B', 'KB', 'MB', 'GB', 'TB'];


  choose(event: any, callback: Function) {
      callback();
  }

  onRemoveTemplatingFile(event: any, file: any, removeFileCallback: Function, index: number) {
      removeFileCallback(event, index);
      this.totalSize -= file?.size ?? 0;
      this.totalSizePercent = this.computePercent();
  }

  onClearTemplatingUpload(clear: Function) {
      clear();
      this.totalSize = 0;
      this.totalSizePercent = 0;
  }

  onSubmit(form: any) {
        if (!this.formatoSelecionado) {
            this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione um formato de conversão.', life: 3000 });
            return;
        }

        if (this.files.length === 0) {
            this.messageService.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione pelo menos um arquivo.', life: 3000 });
            return;
        }

        console.log('Convertendo arquivos:', this.files);
        console.log('Formato selecionado:', this.formatoSelecionado.name);

        // Salva o formato antes de resetar
        const formatoParaConverter = this.formatoSelecionado!.name;
        const arquivosParaConverter = [...this.files];

        // Converte cada arquivo
        arquivosParaConverter.forEach((file, index) => {
            console.log(`Iniciando conversão do arquivo ${index + 1}/${arquivosParaConverter.length}: ${file.name}`);
            
            this.arquivoService.converter(file, formatoParaConverter).subscribe({
                next: (response) => {
                    console.log('Resposta da conversão:', response);
                    
                    this.messageService.add({ 
                        severity: 'success', 
                        summary: 'Sucesso!', 
                        detail: `${file.name} convertido para ${formatoParaConverter}`, 
                        life: 5000 
                    });
                    
                    // Faz download automático
                    if (response.urlDownload) {
                        console.log('Abrindo URL de download:', response.urlDownload);
                        setTimeout(() => {
                            window.open(response.urlDownload, '_blank');
                        }, 500);
                    } else {
                        console.warn('URL de download não encontrada na resposta');
                    }
                },
                error: (err) => {
                    console.error('Erro completo ao converter arquivo:', err);
                    
                    let errorMessage = 'Erro desconhecido';
                    
                    if (err.error) {
                        if (typeof err.error === 'string') {
                            errorMessage = err.error;
                        } else if (err.error.message) {
                            errorMessage = err.error.message;
                        } else if (err.error.error) {
                            errorMessage = err.error.error;
                        }
                    } else if (err.message) {
                        errorMessage = err.message;
                    }
                    
                    if (err.status === 413) {
                        errorMessage = 'Arquivo muito grande. Tamanho máximo: 50MB';
                    }
                    
                    this.messageService.add({ 
                        severity: 'error', 
                        summary: 'Erro na Conversão', 
                        detail: `${file.name}: ${errorMessage}`, 
                        life: 8000 
                    });
                }
            });
        });

        if (form.valid) {
            form.resetForm();
            this.files = [];
            this.totalSize = 0;
            this.totalSizePercent = 0;
        }
    }

  onTemplatedUpload() {
      this.messageService.add({ severity: 'info', summary: 'Success', detail: 'File Uploaded', life: 3000 });
  }

  onSelectedFiles(event: any) {
      this.files = event?.currentFiles ?? [];
      this.totalSize = this.files.reduce((sum, f) => sum + (f?.size ?? 0), 0);
      this.totalSizePercent = this.computePercent();
  }

  uploadEvent(callback: Function) {
      callback();
  }

  private computePercent(): number {
      // Mantive a lógica original (totalSize / 10). Ajuste conforme regra real de percentagem.
      return this.totalSize / 10;
  }

  formatSize(bytes: number): string {
        const k = 1024;
        const sizes = this.fileSizeTypes;
        
        if (!bytes || bytes === 0) {
            return `0 ${sizes[0]}`;
        }

        const i = Math.floor(Math.log(bytes) / Math.log(k));
        const formattedSize = parseFloat((bytes / Math.pow(k, i)).toFixed(2)); // 2 casas decimais

        return `${formattedSize} ${sizes[i] ?? sizes[sizes.length - 1]}`;
    }
}