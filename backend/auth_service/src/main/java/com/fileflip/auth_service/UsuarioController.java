package com.fileflip.auth_service;

import com.fileflip.auth_service.DTOs.LoginRequestDTO;
import com.fileflip.auth_service.DTOs.LoginResponseDTO;
import com.fileflip.auth_service.DTOs.UsuarioRequestDTO;
import com.fileflip.auth_service.DTOs.UsuarioResponseDTO;
import com.fileflip.auth_service.DTOs.VincularGoogleDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuários", description = "Gerencia as operações de usuário")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
        UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder
    ){
        this.usuarioService = usuarioService;
    }

    // Vínculo google
    @Operation(summary = "Vínculo com Google", description = "O usuário vincula sua conta do FileFlip com alguma do Google")
    @PostMapping("/{id}/vincular-google")
    public ResponseEntity<UsuarioResponseDTO> vincularGoogle(
        @Parameter(description="ID do usuário")
        @PathVariable UUID id,
        @Valid @RequestBody VincularGoogleDTO googleDTO
    ) {
        UsuarioResponseDTO usuarioAtualizado = usuarioService.vincularGoogle(id, 
            googleDTO.getGoogleId(),
            googleDTO.getGoogleName(),
            googleDTO.getGooglePictureUrl(),
            googleDTO.getGoogleAccessToken(),
            googleDTO.getGoogleRefreshToken()
        );

        return ResponseEntity.ok(usuarioAtualizado);
    }

    // Login
    @Operation(summary = "Login", description = "Autentica o usuário no sistema")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest
    ) {
        LoginResponseDTO response = usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok()
    }

//    Criação de novo usuário
    @Operation(summary = "Cadastrar novo usuário", description = "Registra um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Dados fornecidos inválidos.")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(
            @Parameter(description = "Dados do usuário")
            @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        UsuarioResponseDTO usuarioCriado = usuarioService.criar(usuarioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @Operation(summary = "Atualizar usuário existente", description = "Atualiza os dados de um usuário existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PutMapping ("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id,
            @Parameter(description = "Novos dados do usuário") @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO){
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizar(id, usuarioRequestDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @Operation(summary = "Listar usuários existentes", description = "Lista todos os usuários cadastrados no sistema")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){
        List<UsuarioResponseDTO> usuarios = usuarioService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

    @Operation(summary = "Deletar usuário", description = "Deletar um usuário cadastrado no sistema")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
        @Parameter(description =  "ID do usuário que será deletado", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6") @PathVariable UUID id){
            usuarioService.deletar(id);
            return ResponseEntity.noContent().build();
        }
}
