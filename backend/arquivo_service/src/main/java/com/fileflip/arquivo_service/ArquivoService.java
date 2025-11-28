package com.fileflip.arquivo_service;

import com.fileflip.arquivo_service.DTOs.ArquivoResponse;
import com.fileflip.arquivo_service.DTOs.ConversaoRequest;
import com.fileflip.arquivo_service.DTOs.ConversaoResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ArquivoService {

    @Value("${convertio.apikey}")
    private String convertioApiKey;

    private final ArquivoRepository arquivoRepository;
    private final ArquivoMapper arquivoMapper;

    public ArquivoService(ArquivoMapper arquivoMapper, ArquivoRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
        this.arquivoMapper = arquivoMapper;
    }

    @Transactional
    public ConversaoResponse converterArquivo(ConversaoRequest request) throws IOException {
        MultipartFile arquivo = request.getArquivo();
        ArquivoType novoTipo = request.getNovoTipo();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String base64File = java.util.Base64.getEncoder().encodeToString(arquivo.getBytes());

        Map<String, Object> body = Map.of(
                "apikey", convertioApiKey,
                "input", "base64",
                "file", base64File,
                "filename", arquivo.getOriginalFilename(),
                "outputformat", novoTipo.name().toLowerCase()
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 1) cria o job na Convertio
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://api.convertio.co/convert",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        @SuppressWarnings("unchecked")
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("data")) {
            throw new IllegalStateException("Resposta inválida da Convertio");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
        String jobId = (String) data.get("id");

        // 2) polling de status até ter URL ou estourar o tempo
        String statusUrl = "https://api.convertio.co/convert/" + jobId + "/status";

        String downloadUrl = null;
        long tamanhoConvertido = 0L;

        for (int i = 0; i < 15; i++) { // tenta ~15 vezes
            ResponseEntity<Map<String, Object>> statusResponse = restTemplate.exchange(
                    statusUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> statusBody = statusResponse.getBody();
            System.out.println("STATUS CONVERTIO: " + statusBody);

            if (statusBody == null || !statusBody.containsKey("data")) {
                throw new IllegalStateException("Resposta de status inválida da Convertio");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> statusData = (Map<String, Object>) statusBody.get("data");
            String step = (String) statusData.get("step"); // "convert" ou "finish"

            Object outputObj = statusData.get("output");
            if ("finish".equalsIgnoreCase(step) && outputObj instanceof Map<?, ?> outputMap) {
                Object urlObj = outputMap.get("url");
                Object sizeObj = outputMap.get("size");

                if (urlObj instanceof String urlStr) {
                    downloadUrl = urlStr;
                }

                if (sizeObj instanceof Number n) {
                    tamanhoConvertido = n.longValue();
                } else if (sizeObj instanceof String s) {
                    try {
                        tamanhoConvertido = Long.parseLong(s);
                    } catch (NumberFormatException e) {
                        tamanhoConvertido = 0L; // fallback
                    }
                }

                if (downloadUrl != null) {
                    break;
                }
            }


            try {
                Thread.sleep(500); // 500ms - polling mais rápido
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Polling interrompido", e);
            }
        }

        if (downloadUrl == null) {
            throw new IllegalStateException("Conversão não finalizou dentro do tempo esperado");
        }

        // 3) pegar ID do usuário autenticado a partir do JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Usuário não autenticado ou token inválido");
        }

        // "sub" contém o UUID do usuário
        String userIdStr = jwt.getClaim("sub");
        if (userIdStr == null || userIdStr.isBlank()) {
            throw new IllegalStateException("Claim 'sub' ausente ou vazio no token");
        }

        UUID usuarioId = UUID.fromString(userIdStr);

        // continua igual
        boolean possuiFoto = novoTipo == ArquivoType.JPG
                || novoTipo == ArquivoType.JPEG
                || novoTipo == ArquivoType.PNG
                || novoTipo == ArquivoType.WEBP;

        Arquivo entidade = arquivoMapper.toEntity(
                arquivo.getOriginalFilename(),
                novoTipo,
                tamanhoConvertido,
                usuarioId,
                possuiFoto,
                downloadUrl
        );

        arquivoRepository.save(entidade);

        // 4) response para o frontend
        return new ConversaoResponse(
                "Conversão concluída. Id: " + jobId,
                downloadUrl
        );
    }

    public List<ArquivoResponse> listar() {

        return arquivoRepository.findAll()
                .stream()
                .map(arquivoMapper::toResponseDTO)
                .toList();
    }

    public List<ArquivoResponse> listarPorUsuario(UUID id) {
        List<Arquivo> arquivos = arquivoRepository.findByUsuarioId(id);
        
        System.out.println("=== LISTAR POR USUARIO ===");
        System.out.println("Total de arquivos encontrados: " + arquivos.size());
        for (Arquivo arq : arquivos) {
            System.out.println("Arquivo ID: " + arq.getArquivo_id());
            System.out.println("Nome: " + arq.getName());
            System.out.println("URL Download (entity): " + arq.getUrlDownload());
        }
        System.out.println("==========================");
        
        return arquivos.stream()
                .map(arquivoMapper::toResponseDTO)
                .toList();
    }
}
