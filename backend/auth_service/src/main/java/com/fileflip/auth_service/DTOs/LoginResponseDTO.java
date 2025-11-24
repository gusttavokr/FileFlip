package com.fileflip.auth_service.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email;
    private String username;
}
