package com.fileflip.arquivo_service.DTOs;

import com.fileflip.arquivo_service.ArquivoType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisição dos arquivos")
public class ArquivoRequest {

    @NotBlank(message = "O campo 'nome' é obrigatório.")
    @Size(min=1, max = 255, message = "O campo deve ter entre 1 a 255 caracteres")
    @Schema(description = "Nome do arquivo", example = "ArtigoFaculdade")
    private String name;

    @NotNull(message = "O arquivo deve ter um tipo.")
    @Schema(description = "Tipo do arquivo", example = "PDF")
    private ArquivoType tipo_arquivo;

    @NotNull(message = "O campo 'tamanho' é obrigatório")
    @Schema(description = "Tamanho do arquivo", example = "2048000")
    private Long tamanhoArquivo;

    @NotNull(message = "O campo 'usuário_id' é obrigatório")
    @Schema(description = "ID do usuário", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID usuarioId;

    @Schema(description = "Imagem do arquivo", example = "true")
    private boolean possuiFoto;
}
