package com.fileflip.auth_service.DTOs;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VincularGoogleDTO {
    private String googleAccessToken;
    private String googleRefreshToken;
    private String googleId;
    private String googleName;
    private String googlePictureUrl;
}
