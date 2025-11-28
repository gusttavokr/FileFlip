package com.fileflip.auth_service.DTOs;

import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private UUID userId;
    private String token;
    private String email;
    private String username;
}
