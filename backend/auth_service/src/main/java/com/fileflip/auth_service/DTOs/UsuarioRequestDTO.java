package com.fileflip.auth_service.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para requisição de  criação ou atualização de um usuário")
public class UsuarioRequestDTO {

    @Size(min = 3, max = 100, message = "O campo 'nome' deve ter entre 3 e 100 caracteres.")
    @Schema(description = "Nome do usuário.", example = "Eduardo Braulio")
    private String username;

    @NotBlank(message = "O campo 'email' é obrigatório.")
    @Email(message = "O campo 'email' deve conter um endereço de e-mail válido.")
    @Schema(description = "E-mail de usuário", example = "eduardobraulio@gmail.com")
    private String email;

    @NotBlank(message = "O campo 'senha' é obrigatório.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Schema(description = "Senha de acesso do usuário, com no mínimo 6 caracteres.", example = "minhaSenhaSegura123")
    private String password;

    @NotBlank(message = "O campo 'senha' é obrigatório.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    @Schema(description = "Senha de acesso do usuário, com no mínimo 6 caracteres.", example = "minhaSenhaSegura123")
    private String confirmPassword;

    @AssertTrue(message="As senhas não são iguais.")
    public boolean SenhasIguais(){
        if (password == null || confirmPassword == null){
            return false;
        }
        return password.equals(confirmPassword);
    }
}
