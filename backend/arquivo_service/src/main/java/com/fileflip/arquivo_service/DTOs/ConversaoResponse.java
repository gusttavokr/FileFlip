package com.fileflip.arquivo_service.DTOs;

//import com.fileflip.arquivo_service.ArquivoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversaoResponse {

    private String mensagem;
    private String urlDownload;
}
