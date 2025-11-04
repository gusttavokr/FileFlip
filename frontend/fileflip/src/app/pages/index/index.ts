// ...existing code...
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
        if (form.valid) {
            this.messageService.add({ severity: 'success', summary: 'Sucesso!', detail: 'Seus arquivos estão sendo convertidos.', life: 3000 });
            form.resetForm();
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
      const dm = 3;
      const sizes = this.fileSizeTypes;
      if (!bytes || bytes === 0) {
          return `0 ${sizes[0]}`;
      }

      const i = Math.floor(Math.log(bytes) / Math.log(k));
      const formattedSize = parseFloat((bytes / Math.pow(k, i)).toFixed(dm));

      return `${formattedSize} ${sizes[i] ?? sizes[sizes.length - 1]}`;
  }
}
// ...existing code...