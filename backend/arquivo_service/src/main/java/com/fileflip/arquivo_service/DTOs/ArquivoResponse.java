package com.fileflip.arquivo_service.DTOs;

import com.fileflip.arquivo_service.ArquivoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoResponse {
    private String name;
    private ArquivoType tipoArquivo;
    private Long tamanhoArquivo;
    private boolean possuiFoto;
    private UUID usuarioId;
    private UUID arquivoId;
}
