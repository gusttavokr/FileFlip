package com.fileflip.arquivo_service;

public enum Tipo {
    PDF("Pdf"),
    DOC("Doc"),
    DOCX("Docx"),
    TXT("Txt"),

    PNG("Png"),
    JPG("Jpg"),
    JPEG("Jpeg"),
    SVG("Svg"),

    MP4("Mp4"),
    MP3("Mp3"),
    ZIP("Zip"),
    RAR("Rar");


    private final String label;

    Tipo(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
