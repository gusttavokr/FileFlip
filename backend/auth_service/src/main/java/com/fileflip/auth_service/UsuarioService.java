package com.fileflip.auth_service;

import com.fileflip.auth_service.DTOs.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          BCryptPasswordEncoder passwordEncoder,
                          JwtEncoder jwtEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    // Criar usuário
    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto){
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
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
    public VincularGoogleResponseDTO vincularGoogle(UUID id, String googleId,
                                                   String googleName, String googlePictureUrl, String googleAccessToken, String googleRefreshToken){

            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário " + id + " não foi encontrado"));

            usuario.setGoogleId(googleId);
            usuario.setGoogleName(googleName);
            usuario.setGooglePictureUrl(googlePictureUrl);
            usuario.setGoogleAccessToken(googleAccessToken);
            usuario.setGoogleRefreshToken(googleRefreshToken);
            usuario.setGoogleVinculado(true);

            usuarioRepository.save(usuario);

            VincularGoogleResponseDTO resposta = usuarioMapper.toGoogleDTO(usuario);

            return resposta;
    }

    // Login
    public LoginResponseDTO login(String email, String password){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        var now = Instant.now();
        var expiresIn = jwtExpiration / 1000; // Converte de milissegundos para segundos

        var claims = JwtClaimsSet.builder()
                .issuer("fileflip_backend")
                .subject(usuario.getUserId().toString())
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var JwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new LoginResponseDTO(JwtValue, usuario.getEmail(), usuario.getUsername());

    }
}
