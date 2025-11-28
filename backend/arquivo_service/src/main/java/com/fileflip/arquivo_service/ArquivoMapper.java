package com.fileflip.arquivo_service;

import com.fileflip.arquivo_service.DTOs.ArquivoResponse;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ArquivoMapper {

    public Arquivo toEntity(String nome, ArquivoType tipoArquivo,
                            Long tamanhoArquivo, UUID usuarioId,
                            boolean possuiFoto, String urlDownload) {

        Arquivo arquivo = new Arquivo();
        arquivo.setName(nome);
        arquivo.setTipoArquivo(tipoArquivo);
        arquivo.setTamanhoArquivo(tamanhoArquivo);
        arquivo.setUsuarioId(usuarioId);
        arquivo.setPossuiFoto(possuiFoto);
        arquivo.setUrlDownload(urlDownload);
        return arquivo;
    }

    public ArquivoResponse toResponseDTO(Arquivo arquivo){
        if (arquivo == null){
            return null;
        }

        System.out.println("=== MAPPER toResponseDTO ===");
        System.out.println("Arquivo entity:");
        System.out.println("  ID: " + arquivo.getArquivo_id());
        System.out.println("  Nome: " + arquivo.getName());
        System.out.println("  urlDownload (entity): " + arquivo.getUrlDownload());
        System.out.println("  Método getUrlDownload() existe? " + (arquivo.getUrlDownload() != null ? "SIM" : "NULL"));

        var dto = new ArquivoResponse();
        dto.setArquivoId(arquivo.getArquivo_id());
        dto.setUsuarioId(arquivo.getUsuarioId());
        dto.setName(arquivo.getName());
        dto.setTipoArquivo(arquivo.getTipoArquivo());
        dto.setTamanhoArquivo(arquivo.getTamanhoArquivo());
        dto.setUrlDownload(arquivo.getUrlDownload());
        dto.setPossuiFoto(arquivo.isPossuiFoto());

        System.out.println("DTO criado:");
        System.out.println("  urlDownload (dto): " + dto.getUrlDownload());
        System.out.println("============================");

        return dto;
    }
}
