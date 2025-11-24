package com.fileflip.arquivo_service;

import com.fileflip.arquivo_service.DTOs.ArquivoResponse;
import com.fileflip.arquivo_service.DTOs.ConversaoRequest;
import com.fileflip.arquivo_service.DTOs.ConversaoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

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

    @GetMapping("/api/arquivos")
    public ResponseEntity<List<ArquivoResponse>> getArquivosByUsuario(@RequestParam UUID usuarioId) {
        List<ArquivoResponse> arquivos = arquivoService.listarPorUsuario(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(arquivos);
    }

}