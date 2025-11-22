package com.fileflip.arquivo_service.DTOs;

import com.fileflip.arquivo_service.ArquivoType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversaoRequest {

    @NotNull(message = "O campo 'novo tipo' é obrigatório")
    @Schema(description = "Tipo do novo arquivo", example = "DOCX")
    private ArquivoType novoTipo;

    @NotNull(message = "O campo 'arquivo' é obrigatório")
    @Schema(description = "Dados do arquivo original")
    private MultipartFile arquivo;
//    @Schema(description = "ID do arquivo a ser convertido", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
//    private UUID arquivoId;
}
