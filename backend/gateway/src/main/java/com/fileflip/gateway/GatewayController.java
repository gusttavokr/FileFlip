package com.fileflip.gateway;

import com.fileflip.soap_service.BuscarArquivoRequest;
import com.fileflip.soap_service.BuscarArquivoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final WebClient arquivoWebClient;
    private final WebClient authWebClient;
    private final WebServiceTemplate soapTemplate;

    public GatewayController(WebClient arquivoWebClient,
                             WebClient authWebClient,
                             WebServiceTemplate soapTemplate) {
        this.arquivoWebClient = arquivoWebClient;
        this.authWebClient = authWebClient;
        this.soapTemplate = soapTemplate;
    }

    // ===== arquivo_service (REST) =====

    @GetMapping("/arquivos")
    public ResponseEntity<?> listarArquivos(@RequestParam UUID usuarioId) {
        Object arquivos = arquivoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/arquivos")
                        .queryParam("usuarioId", usuarioId)
                        .build())
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(arquivos);
    }

    // (quando quiser, dá pra adicionar o /arquivos/converter com multipart)

    // ===== auth_service (REST) =====

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        Object resp = authWebClient.post()
                .uri("/api/v1/usuarios/login")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/auth/usuarios")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Map<String, Object> body) {
        Object resp = authWebClient.post()
                .uri("/api/v1/usuarios")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/auth/usuarios/{id}/vincular-google")
    public ResponseEntity<?> vincularGoogle(@PathVariable UUID id,
                                            @RequestBody Map<String, Object> body) {
        Object resp = authWebClient.post()
                .uri("/api/v1/usuarios/{id}/vincular-google", id)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
        return ResponseEntity.ok(resp);
    }

    // ===== soap_service (SOAP) =====

    @GetMapping("/soap/arquivos")
    public ResponseEntity<?> listarArquivosSoap(@RequestParam UUID usuarioId) {
        BuscarArquivoRequest request = new BuscarArquivoRequest();
        request.setUsuarioId(usuarioId.toString());

        BuscarArquivoResponse response =
                (BuscarArquivoResponse) soapTemplate.marshalSendAndReceive(request);
        return ResponseEntity.ok(response.getArquivos());
    }
}
