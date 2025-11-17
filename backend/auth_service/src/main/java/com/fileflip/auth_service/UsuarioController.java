package com.fileflip.auth_service;

import com.fileflip.auth_service.DTOs.UsuarioRequestDTO;
import com.fileflip.auth_service.DTOs.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Gerencia as operações de usuário")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

//    Criação de novo usuário
    @Operation(summary = "Criar novo usuário", description = "Registra um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Dados fornecidos inválidos.")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(
            @Parameter(description = "Dados do usuário")
            @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO usuarioCriado = usuarioService.criar(usuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @Operation(summary = "Atualizar usuário existente", description = "Atualiza os dados de um usuário existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping ("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado", example = "1") @PathVariable UUID id,
            @Parameter(description = "Novos dados do usuário") @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO){
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizar(id, usuarioRequestDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

//    @GetMapping
//    public ResponseEntity<UsuarioResponseDTO> listarUsuarios()
}
