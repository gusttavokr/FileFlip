package com.fileflip.arquivo_service;

import com.fileflip.arquivo_service.DTOs.ArquivoRequest;
import com.fileflip.arquivo_service.DTOs.ArquivoResponse;
import com.fileflip.arquivo_service.DTOs.ConversaoRequest;
import com.fileflip.arquivo_service.DTOs.ConversaoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class ArquivoMapper {

    public Arquivo toEntity(String nome, ArquivoType tipoArquivo,
                            Long tamanhoArquivo, UUID usuarioId,
                            boolean possuiFoto) {

        Arquivo arquivo = new Arquivo();
        arquivo.setName(nome);
        arquivo.setTipoArquivo(tipoArquivo);
        arquivo.setTamanhoArquivo(tamanhoArquivo);
        arquivo.setUsuarioId(usuarioId);
        arquivo.setPossuiFoto(possuiFoto);
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
