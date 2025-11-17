package com.fileflip.auth_service;

//import com.fileflip.arquivo_service.Arquivo;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name="tb_users")
public class Usuario {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name="user_id")
    private UUID userId;

    @Column (nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column (nullable = false)
    private String password;

    // Um usuário pode ter muitos arquivos
    // @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // private List<Arquivo> arquivos;
}