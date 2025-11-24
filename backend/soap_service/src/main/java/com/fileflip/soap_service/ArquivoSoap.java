package com.fileflip.soap_service;

public class ArquivoSoap {
    private String arquivo_id;
    private String name;
    private String tipoArquivo;
    private Long tamanhoArquivo;
    private String usuario_id;
    private boolean possuiFoto;

    public ArquivoSoap() {}

    public String getArquivo_id() { return arquivo_id; }
    public void setArquivo_id(String arquivo_id) { this.arquivo_id = arquivo_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public Long getTamanhoArquivo() { return tamanhoArquivo; }
    public void setTamanhoArquivo(Long tamanhoArquivo) { this.tamanhoArquivo = tamanhoArquivo; }

    public String getUsuario_id() { return usuario_id; }
    public void setUsuario_id(String usuario_id) { this.usuario_id = usuario_id; }

    public boolean isPossuiFoto() { return possuiFoto; }
    public void setPossuiFoto(boolean possuiFoto) { this.possuiFoto = possuiFoto; }
}
