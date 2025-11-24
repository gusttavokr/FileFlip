    package com.fileflip.soap_service;

    import jakarta.xml.bind.annotation.XmlRootElement;
    import jakarta.xml.bind.annotation.XmlElement;
    import java.util.List;

    @XmlRootElement(name = "BuscarArquivoResponse", namespace = "http://fileflip.com/arquivo")
    public class BuscarArquivoResponse {
        private List<ArquivoSoap> arquivos;

        public BuscarArquivoResponse() {}

        @XmlElement(name = "arquivos")
        public List<ArquivoSoap> getArquivos() {
            return arquivos;
        }
        public void setArquivos(List<ArquivoSoap> arquivos) {
            this.arquivos = arquivos;
        }
    }
