# 🐳 Instruções para Rodar o FileFlip no Docker

## ✅ Problemas Corrigidos

- **CRÍTICO**: Nomes de serviços com underscore que o Tomcat rejeitava
- URLs de comunicação entre serviços atualizadas
- CORS e ALLOWED_HOSTS configurados para Docker
- Healthchecks implementados com ordem de inicialização
- Suporte ao arquivo .env adicionado

## 🚀 Como Iniciar

### 1. Rebuild dos containers (necessário após as correções)

```powershell
cd C:\Users\gusta\Downloads\FileFlip

# Para e remove os containers antigos
docker-compose down -v

# Rebuild completo (pode demorar alguns minutos)
docker-compose build --no-cache

# Inicia todos os serviços
docker-compose up -d
```

### 2. Acompanhar os logs

```powershell
# Ver logs de todos os serviços
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f auth-service
docker-compose logs -f arquivo-service
docker-compose logs -f soap-service
docker-compose logs -f gateway
docker-compose logs -f frontend
```

### 3. Verificar o status

```powershell
# Ver status de todos os containers
docker-compose ps

# Verificar se estão saudáveis (healthy)
docker ps
```

### 4. Acessar a aplicação

- **Frontend**: http://localhost:4200
- **Gateway API**: http://localhost:8000
- **Gateway Swagger**: http://localhost:8000/swagger/
- **Auth Service**: http://localhost:8081
- **Arquivo Service**: http://localhost:8082
- **SOAP Service**: http://localhost:8083

## 📝 Ordem de Inicialização

Os serviços iniciam nesta ordem (graças aos healthchecks):

1. **Postgres** (deve estar healthy primeiro)
2. **Auth, Arquivo e SOAP Services** (esperam Postgres)
3. **Gateway** (espera todos os backends)
4. **Frontend** (espera Gateway)

**Tempo esperado**: 1-3 minutos para todos ficarem healthy

## 🔍 Troubleshooting

### Se algum serviço não iniciar:

```powershell
# Ver logs detalhados
docker-compose logs <nome-do-servico>

# Reiniciar um serviço específico
docker-compose restart <nome-do-servico>

# Rebuild de um serviço específico
docker-compose up -d --build <nome-do-servico>
```

### Se o banco não conectar:

```powershell
# Verificar se o Postgres está rodando
docker exec -it postgres psql -U postgres -c "\l"

# Ver se os bancos foram criados
docker exec -it postgres psql -U postgres -c "\l" | grep -E "auth_db|arquivo_db"
```

### Para limpar tudo e recomeçar:

```powershell
# CUIDADO: Isso remove TODOS os dados!
docker-compose down -v
docker system prune -a --volumes -f
docker-compose up -d --build
```

## ⚙️ Configurações Importantes

### Arquivo .env
O arquivo `.env` na raiz já está configurado com:
- `GOOGLE_CLIENT_ID`
- `GOOGLE_CLIENT_SECRET`

### Portas Utilizadas
Certifique-se que estas portas estão livres:
- **4200**: Frontend Angular
- **8000**: Gateway Django
- **8081**: Auth Service
- **8082**: Arquivo Service
- **8083**: SOAP Service
- **5432**: PostgreSQL

### Comunicação entre Serviços

Os serviços se comunicam usando os nomes dos containers:
- `auth-service:8081`
- `arquivo-service:8082`
- `soap-service:8083`
- `postgres:5432`
- `gateway:8000`
- `frontend:4200`

**⚠️ IMPORTANTE**: Não use `localhost` dentro dos containers - use os nomes dos serviços!

## 🎯 Checklist de Sucesso

- [ ] Todos os containers estão com status "Up"
- [ ] Healthchecks mostram "(healthy)" no `docker ps`
- [ ] Frontend carrega em http://localhost:4200
- [ ] Gateway Swagger funciona em http://localhost:8000/swagger/
- [ ] Consegue fazer login/cadastro
- [ ] Conversão de arquivos funciona

## 📚 Comandos Úteis

```powershell
# Ver uso de recursos
docker stats

# Entrar no bash de um container
docker exec -it <nome-container> sh

# Ver redes Docker
docker network ls

# Inspecionar rede do projeto
docker network inspect fileflip_default

# Remover containers parados
docker container prune

# Remover imagens não usadas
docker image prune
```
