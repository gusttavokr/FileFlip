package com.fileflip.arquivo_service;

import com.fileflip.arquivo_service.DTOs.ConversaoRequest;
import com.fileflip.arquivo_service.DTOs.ConversaoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ArquivoController {

    private final ArquivoService arquivoService;

    public ArquivoController(ArquivoService arquivoService){
        this.arquivoService = arquivoService;
    }

    @PostMapping(value = "/converter", consumes = "multipart/form-data")
    public ResponseEntity<ConversaoResponse> converterArquivo(@ModelAttribute ConversaoRequest request) throws IOException {
        ConversaoResponse response = arquivoService.converterArquivo(request);
        return ResponseEntity.ok(response);
    }
}