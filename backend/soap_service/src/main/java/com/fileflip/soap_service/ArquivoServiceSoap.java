package com.fileflip.soap_service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;

@Component
public class ArquivoServiceSoap {

    private final RestTemplate restTemplate;
    private final ArquivoMapper arquivoMapper;

    public ArquivoServiceSoap() {
        this.restTemplate = new RestTemplate();
        this.arquivoMapper = new ArquivoMapper();
    }

    public List<ArquivoSoap> buscarArquivosDoUsuario(UUID usuarioId) {
        System.out.println("Entrei na service");
        String url = "http://localhost:8080/api/arquivos?usuarioId=" + usuarioId;
        System.out.println("URL chamada: " + url);
        ResponseEntity<List<ArquivoResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ArquivoResponse>>() {}
        );

        List<ArquivoResponse> arquivoResponses = response.getBody();
        if (arquivoResponses == null) {
            return List.of();
        }
        return arquivoResponses.stream()
                .map(arquivoMapper::toArquivoSoap)
                .toList();
    }
}
