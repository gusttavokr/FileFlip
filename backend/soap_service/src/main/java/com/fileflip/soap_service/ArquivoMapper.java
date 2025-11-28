package com.fileflip.soap_service;

public class ArquivoMapper {

    public ArquivoSoap toArquivoSoap(ArquivoResponse response) {
        ArquivoSoap soap = new ArquivoSoap();
        soap.setArquivo_id(String.valueOf(response.getArquivoId()));
        soap.setName(response.getName());
        soap.setTamanhoArquivo(response.getTamanhoArquivo());
        soap.setUsuario_id(String.valueOf(response.getUsuarioId()));
        soap.setPossuiFoto(response.isPossuiFoto());
        soap.setUrlDownload(response.getUrlDownload());
        
        System.out.println("=== MAPPER SOAP ===");
        System.out.println("ArquivoResponse.getUrlDownload(): " + response.getUrlDownload());
        System.out.println("ArquivoSoap.getUrlDownload(): " + soap.getUrlDownload());
        System.out.println("===================");
        
        return soap;
    }
}
