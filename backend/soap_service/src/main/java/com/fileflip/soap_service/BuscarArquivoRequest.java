package com.fileflip.soap_service;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"usuarioId"})
@XmlRootElement(name = "BuscarArquivoRequest", namespace = "http://fileflip.com/arquivo")
public class BuscarArquivoRequest {

    @XmlElement(name = "usuarioId", namespace = "http://fileflip.com/arquivo", required = true)
    protected String usuarioId;

    public String getUsuarioId() { return usuarioId; }

    public void setUsuarioId(String value) { this.usuarioId = value; }
}
