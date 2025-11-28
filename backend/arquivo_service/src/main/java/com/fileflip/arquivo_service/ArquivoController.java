package com.fileflip.arquivo_service;

import com.fileflip.arquivo_service.DTOs.ArquivoResponse;
import com.fileflip.arquivo_service.DTOs.ConversaoRequest;
import com.fileflip.arquivo_service.DTOs.ConversaoResponse;
import com.fileflip.arquivo_service.DTOs.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ArquivoController {

    private final ArquivoService arquivoService;

    public ArquivoController(ArquivoService arquivoService){
        this.arquivoService = arquivoService;
    }

    @PostMapping(value = "/converter", consumes = "multipart/form-data")
    public ResponseEntity<?> converterArquivo(@ModelAttribute ConversaoRequest request) {
        try {
            ConversaoResponse response = arquivoService.converterArquivo(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Erro na conversão: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar arquivo: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro desconhecido: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ArquivoResponse>> getArquivosByUsuario(@RequestParam UUID usuarioId) {
        List<ArquivoResponse> arquivos = arquivoService.listarPorUsuario(usuarioId);
        return ResponseEntity.status(HttpStatus.OK).body(arquivos);
    }
}
