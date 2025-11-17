package com.fileflip.auth_service;

import com.fileflip.auth_service.DTOs.UsuarioRequestDTO;
import com.fileflip.auth_service.DTOs.UsuarioResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toEntity(UsuarioRequestDTO dto){
        if (dto == null){
            return null;
        }

        var usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario){
        if (usuario == null){
            return null;
        }

        var dto = new UsuarioResponseDTO();
        dto.setId(usuario.getUserId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());

        return dto;
    }
}
