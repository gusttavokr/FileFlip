package com.fileflip.soap_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.UUID;

@Endpoint
public class ArquivoEndpoint {

    private static final String NAMESPACE_URI = "http://fileflip.com/arquivo";

    @Autowired
    private ArquivoServiceSoap arquivoServiceSoap;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "BuscarArquivoRequest")
    @ResponsePayload
    public BuscarArquivoResponse buscarArquivos(@RequestPayload BuscarArquivoRequest request) {
        System.out.println("Request: " + request);
        System.out.println("Class: " + request.getClass().getName());
        System.out.println("usuarioId recebido do request SOAP: " + request.getUsuarioId());
        if (request.getUsuarioId() == null || request.getUsuarioId().isEmpty()) {
            throw new IllegalArgumentException("usuarioId não enviado ou está vazio!");
        }
        UUID uuid = UUID.fromString(request.getUsuarioId());
        List<ArquivoSoap> arquivos = arquivoServiceSoap.buscarArquivosDoUsuario(uuid);
        BuscarArquivoResponse response = new BuscarArquivoResponse();
        response.setArquivos(arquivos);
        return response;
    }
}