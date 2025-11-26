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

        var dto = new ArquivoResponse();
        dto.setArquivoId(arquivo.getArquivo_id());
        dto.setUsuarioId(arquivo.getUsuarioId());
        dto.setName(arquivo.getName());
        dto.setTipoArquivo(arquivo.getTipoArquivo());
        dto.setTamanhoArquivo(arquivo.getTamanhoArquivo());

        return dto;
    }
}
