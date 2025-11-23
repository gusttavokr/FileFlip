package com.fileflip.arquivo_service;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name="tb_arquivos")
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="arquivo_id")
    private UUID arquivo_id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArquivoType tipoArquivo;

    @Column(nullable = false)
    private Long tamanhoArquivo;

    @Column(nullable = false)
    private UUID usuario_id;

    @Column
    private boolean possuiFoto;
}
