package com.fileflip.soap_service;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BuscarArquivoRequest", namespace = "http://fileflip.com/arquivo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BuscarArquivoRequest {

    @XmlElement(name = "usuarioId", namespace = "http://fileflip.com/arquivo", required = true)
    private String usuarioId;
    
    @XmlElement(name = "token", namespace = "http://fileflip.com/arquivo", required = true)
    private String token;

    public String getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
