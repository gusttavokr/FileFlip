package com.fileflip.auth_service;

import com.fileflip.auth_service.DTOs.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fileflip.auth_service.*;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper){
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    // Criar usuário
    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto){
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuario);
    }

    // Atualizar usuário
    @Transactional
    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto){

        // Buscando por ID
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário " + id + " não encontrado."));

        // Validando e Atualizando inputs
        if (dto.getUsername() != null && !dto.getUsername().isBlank()){
            usuario.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()){
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()){
            usuario.setPassword(dto.getPassword());
        }

        return usuarioMapper.toResponseDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listar() {

        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    // Deletar usuário
    @Transactional
    public void deletar(UUID id){
        if (!usuarioRepository.existsById(id)){
            throw new EntityNotFoundException("O usuário de id: " + id + " não foi encontrado.");
        }
        usuarioRepository.deleteById(id);
    }

    // Autenticar com o Google
    @Transactional
    public UsuarioResponseDTO vincularGoogle(UUID id, String googleId, 
        String googleName, String googlePictureUrl,
        String googleAccessToken, String googleRefreshToken){

            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário " + id + " não foi encontrado"));

            usuario.setGoogleId(googleId);
            usuario.setGoogleName(googleName);
            usuario.setGooglePictureUrl(googlePictureUrl);
            usuario.setGoogleAccessToken(googleAccessToken);
            usuario.setGoogleRefreshToken(googleRefreshToken);

            usuarioRepository.save(usuario);

            UsuarioResponseDTO response = usuarioMapper.toResponseDTO(usuario);

            return response;
    }

    // Login
    public LoginResponseDTO login(String email, String password){
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!PasswordEncoder.matches(password, usuario.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        } 

        
    }
}
