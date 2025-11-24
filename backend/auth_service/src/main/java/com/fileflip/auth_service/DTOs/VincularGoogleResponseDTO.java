package com.fileflip.auth_service.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VincularGoogleResponseDTO {

    private String googleId;
    private String googleName;
    private String googlePictureUrl;
    private Boolean googleVinculado;
}
