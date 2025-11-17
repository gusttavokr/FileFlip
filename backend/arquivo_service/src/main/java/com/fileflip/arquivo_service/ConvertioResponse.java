package com.fileflip.arquivo_service;

import lombok.Data;

@Data
public class ConvertioResponse {
    private String id;
    private String status;
    private String input;
    private String output;
    private int step;
}
