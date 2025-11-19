package com.fileflip.auth_service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "sua-chave-secreta";
    private static final long EXPIRATION_TIME = 86400000; // 1 dia em ms

    public String generateToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getUserId().toString())
                .claim("username", usuario.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    // Para validar token, implemente método parse/validate
}
