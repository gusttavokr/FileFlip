package com.fileflip.arquivo_service;

import jakarta.persistence.*;
import lombok.*;

import com.fileflip.auth_service.Usuario;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name="tb_files")
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "file_id")
    private UUID fileId;

    @Column(name="file_name")
    private String nome;

    @Column(name="file_type")
    private Tipo tipo;

    @Column(name="file_size")
    private long tamanho;

    @ManyToOne
    private Usuario usuario;
}
