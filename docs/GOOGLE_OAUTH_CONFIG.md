# 🔐 Configuração do Google OAuth para FileFlip

## ✅ Problema Resolvido: SOAP Service
O SOAP service agora usa `arquivo-service:8082` em vez de `localhost:8082` e deve funcionar corretamente.

## 🔧 Configurando Google OAuth no Docker

### 1. **Atualizar Redirect URIs no Google Cloud Console**

Acesse: https://console.cloud.google.com/apis/credentials

No seu OAuth 2.0 Client ID, adicione as seguintes **URIs de redirecionamento autorizadas**:

```
http://localhost:8081/login/oauth2/code/google
http://auth-service:8081/login/oauth2/code/google
```

### 2. **Como Funciona o Fluxo OAuth Atual**

O FileFlip **NÃO** usa OAuth2 Login no backend Spring Boot. Em vez disso:

1. **Frontend** faz a autenticação OAuth com o Google
2. **Frontend** recebe os dados do Google (googleId, accessToken, refreshToken, etc.)
3. **Frontend** envia esses dados para o endpoint `/gateway/auth/{userId}/vincular-google`
4. **Backend** salva os dados do Google associados ao usuário

### 3. **Implementação no Frontend (Recomendado)**

#### Opção A: Usar biblioteca JavaScript para Google OAuth

Instale no frontend:
```bash
npm install @react-oauth/google
# ou
npm install gapi-script
```

Exemplo de código Angular para vincular Google:

```typescript
// Adicione no seu componente de perfil
import { environment } from '../environments/environment';

vinculateGoogle() {
  // 1. Inicia fluxo OAuth do Google
  const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?` +
    `client_id=${environment.googleClientId}&` +
    `redirect_uri=${environment.redirectUri}&` +
    `response_type=code&` +
    `scope=openid email profile https://www.googleapis.com/auth/drive.file&` +
    `access_type=offline&` +
    `prompt=consent`;
  
  window.location.href = googleAuthUrl;
}

// 2. No componente de callback (após redirect do Google)
handleGoogleCallback() {
  const urlParams = new URLSearchParams(window.location.search);
  const code = urlParams.get('code');
  
  if (code) {
    // Trocar code por access_token
    this.exchangeCodeForToken(code).subscribe(tokenData => {
      // 3. Enviar dados para backend
      this.vincularGoogleBackend(tokenData);
    });
  }
}

vincularGoogleBackend(googleData: any) {
  const userId = this.getUserId(); // Pegar do token JWT
  
  const payload = {
    googleId: googleData.sub,
    googleName: googleData.name,
    googlePictureUrl: googleData.picture,
    googleAccessToken: googleData.access_token,
    googleRefreshToken: googleData.refresh_token
  };
  
  this.http.post(
    `http://localhost:8000/gateway/auth/${userId}/vincular-google`,
    payload,
    { headers: { Authorization: `Bearer ${this.getJwtToken()}` } }
  ).subscribe(response => {
    console.log('Google vinculado com sucesso!', response);
  });
}
```

### 4. **Criar environment.ts no Frontend**

```typescript
// src/environments/environment.ts
export const environment = {
  production: false,
  googleClientId: '627640437712-58jhog880v5pm2o6c6qifo3lhgg0ctll.apps.googleusercontent.com',
  redirectUri: 'http://localhost:4200/auth/google/callback',
  apiUrl: 'http://localhost:8000/gateway'
};
```

### 5. **Rota de Callback no Angular**

Crie um componente para processar o callback do Google:

```typescript
// src/app/pages/google-callback/google-callback.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-google-callback',
  template: '<p>Processando autenticação Google...</p>'
})
export class GoogleCallbackComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      if (code) {
        this.processGoogleAuth(code);
      }
    });
  }

  processGoogleAuth(code: string) {
    // Implementar lógica de troca de code por token
    // e vincular no backend
  }
}
```

Adicione a rota:
```typescript
// app.routes.ts
{
  path: 'auth/google/callback',
  component: GoogleCallbackComponent
}
```

### 6. **Alternativa: OAuth no Backend Spring Boot**

Se preferir fazer OAuth pelo backend, você precisa adicionar:

#### No `application.yml` do auth_service:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope: openid, email, profile, https://www.googleapis.com/auth/drive.file
            redirect-uri: "{baseUrl}/login/oauth2/code/google"
            client-name: Google
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://openidconnect.googleapis.com/v1/userinfo
            user-name-attribute: sub
```

#### Criar Controller para OAuth:

```java
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuth2Controller {
    
    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        // Redireciona para Google OAuth
        response.sendRedirect("/oauth2/authorization/google");
    }
    
    @GetMapping("/success")
    public ResponseEntity<LoginResponseDTO> handleOAuthSuccess(
        @AuthenticationPrincipal OAuth2User oauth2User
    ) {
        // Processar dados do Google e retornar JWT
        String email = oauth2User.getAttribute("email");
        String googleId = oauth2User.getAttribute("sub");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        
        // Criar ou atualizar usuário
        // Gerar JWT
        // Retornar resposta
    }
}
```

## 🎯 Resumo

### Solução SOAP ✅
- Corrigido: SOAP agora usa `arquivo-service:8082`
- Reiniciar o container: `docker-compose restart soap-service`

### Solução OAuth Google 🔐
**Recomendação**: Implementar OAuth no **frontend**
- Mais seguro (não expõe client_secret)
- Melhor experiência do usuário
- Menor latência

**Passos**:
1. Atualizar redirect URIs no Google Cloud Console
2. Usar biblioteca JavaScript para OAuth
3. Enviar dados do Google para `/gateway/auth/{userId}/vincular-google`

## 📝 Próximos Passos

1. **Testar SOAP**: Acesse o Swagger do Gateway e teste o endpoint de perfil
2. **Implementar OAuth no Frontend**: Seguir os exemplos acima
3. **Atualizar Google Cloud Console**: Adicionar redirect URIs

## 🆘 Troubleshooting

### SOAP retorna erro 500
```bash
docker logs soap-service
```
Verifique se está conectando em `arquivo-service:8082`

### OAuth redirect não funciona
- Verifique se o redirect_uri está registrado no Google Cloud Console
- Verifique se a porta e host estão corretos
- Use `http://localhost:4200/auth/google/callback` para testes locais
