package com.fileflip.soap_service;

import java.util.UUID;

public class ArquivoResponse {
    private String name;
    private Long tamanhoArquivo;
    private boolean possuiFoto;
    private UUID usuarioId;
    private UUID arquivoId;
    private String urlDownload;

    public ArquivoResponse() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrlDownload() {
        return urlDownload;
    }
    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public Long getTamanhoArquivo() {
        return tamanhoArquivo;
    }
    public void setTamanhoArquivo(Long tamanhoArquivo) {
        this.tamanhoArquivo = tamanhoArquivo;
    }

    public boolean isPossuiFoto() {
        return possuiFoto;
    }
    public void setPossuiFoto(boolean possuiFoto) {
        this.possuiFoto = possuiFoto;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public UUID getArquivoId() {
        return arquivoId;
    }
    public void setArquivoId(UUID arquivoId) {
        this.arquivoId = arquivoId;
    }
}
