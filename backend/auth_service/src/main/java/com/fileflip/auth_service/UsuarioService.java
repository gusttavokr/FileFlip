package com.fileflip.auth_service;

import com.fileflip.auth_service.DTOs.UsuarioRequestDTO;
import com.fileflip.auth_service.DTOs.UsuarioResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // Deletar usuário
    @Transactional
    public void deletar(UUID id){
        if (!usuarioRepository.existsById(id)){
            throw new EntityNotFoundException("O usuário de id: " + id + " não foi encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
}
