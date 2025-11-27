package com.fileflip.soap_service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

    public List<ArquivoSoap> buscarArquivosDoUsuario(UUID usuarioId, String token) {
        System.out.println("Entrei na service");
        String url = "http://localhost:8082/api/v1?usuarioId=" + usuarioId;
        System.out.println("URL chamada: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<ArquivoResponse>> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ArquivoResponse>>() {}
            );
        } catch (Exception e) {
            System.out.println("Erro na chamada REST de arquivos:");
            e.printStackTrace();
            throw e;
        }

        System.out.println("Status REST arquivos: " + response.getStatusCode());
        System.out.println("Body REST arquivos: " + response.getBody());

        List<ArquivoResponse> arquivoResponses = response.getBody();
        if (arquivoResponses == null) {
            return List.of();
        }
        
        // DEBUG: verifica se urlDownload está vindo
        for (ArquivoResponse ar : arquivoResponses) {
            System.out.println("=== ARQUIVO RESPONSE ===");
            System.out.println("Nome: " + ar.getName());
            System.out.println("URL Download: " + ar.getUrlDownload());
            System.out.println("========================");
        }
        
        return arquivoResponses.stream()
                .map(arquivoMapper::toArquivoSoap)
                .toList();
    }

}
