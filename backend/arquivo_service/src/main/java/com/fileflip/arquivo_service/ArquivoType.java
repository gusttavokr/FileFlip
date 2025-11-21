package com.fileflip.arquivo_service;

public enum ArquivoType {
    PDF("Pdf"),
    TXT("Txt"),
    DOCX("Docx"),
    DOC("Doc"),

    JPG("Jpg"),
    JPEG("Jpeg"),
    PNG("Png"),
    SVG("Svg"),
    WEBP("Webp"),

    MP4("Mp4"),
    MP3("Mp3"),
    WAV("Wav"),
    M4A("M4a"),
    ZIP("Zip"),
    RAR("RAR");

    private final String label;

    ArquivoType(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
