Write-Host "=== FileFlip - Configurador de Rede ===" -ForegroundColor Cyan
Write-Host ""

# PASSO 1: Descobrir o IP da máquina
Write-Host "[1/7] Descobrindo seu IP..." -ForegroundColor Yellow
$ip = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.InterfaceAlias -notlike "*Loopback*" -and $_.IPAddress -notlike "169.254.*" } | Select-Object -First 1).IPAddress

if (-not $ip) {
    Write-Host "ERRO: Não foi possível detectar o IP automaticamente." -ForegroundColor Red
    $ip = Read-Host "Digite seu IP manualmente (ex: 192.168.1.100)"
}

Write-Host "   IP detectado: $ip" -ForegroundColor Green
Write-Host ""

# PASSO 2: Parar containers
Write-Host "[2/7] Parando containers existentes..." -ForegroundColor Yellow
docker-compose down
Write-Host "   Containers parados!" -ForegroundColor Green
Write-Host ""

# PASSO 3: Atualizar arquivo usuario.ts
Write-Host "[3/7] Atualizando frontend/fileflip/src/app/service/usuario.ts..." -ForegroundColor Yellow
$usuarioFile = "frontend\fileflip\src\app\service\usuario.ts"
(Get-Content $usuarioFile) -replace "http://localhost:8000", "http://${ip}:8000" | Set-Content $usuarioFile
Write-Host "   Arquivo atualizado!" -ForegroundColor Green
Write-Host ""

# PASSO 4: Atualizar arquivo arquivo.ts
Write-Host "[4/7] Atualizando frontend/fileflip/src/app/service/arquivo.ts..." -ForegroundColor Yellow
$arquivoFile = "frontend\fileflip\src\app\service\arquivo.ts"
(Get-Content $arquivoFile) -replace "http://localhost:8000", "http://${ip}:8000" | Set-Content $arquivoFile
Write-Host "   Arquivo atualizado!" -ForegroundColor Green
Write-Host ""

# PASSO 5: Rebuild frontend
Write-Host "[5/7] Reconstruindo frontend..." -ForegroundColor Yellow
docker-compose build frontend
Write-Host "   Frontend reconstruído!" -ForegroundColor Green
Write-Host ""

# PASSO 6: Subir containers
Write-Host "[6/7] Iniciando todos os containers..." -ForegroundColor Yellow
docker-compose up -d
Write-Host "   Containers iniciados!" -ForegroundColor Green
Write-Host ""

# PASSO 7: Liberar firewall
Write-Host "[7/7] Configurando firewall..." -ForegroundColor Yellow
try {
    New-NetFirewallRule -DisplayName "FileFlip-Frontend" -Direction Inbound -LocalPort 4200 -Protocol TCP -Action Allow -ErrorAction SilentlyContinue | Out-Null
    New-NetFirewallRule -DisplayName "FileFlip-Gateway" -Direction Inbound -LocalPort 8000 -Protocol TCP -Action Allow -ErrorAction SilentlyContinue | Out-Null
    Write-Host "   Firewall configurado!" -ForegroundColor Green
} catch {
    Write-Host "   AVISO: Execute como Administrador para configurar o firewall automaticamente" -ForegroundColor Yellow
}
Write-Host ""

# Resumo final
Write-Host "=== CONFIGURACAO CONCLUIDA! ===" -ForegroundColor Green
Write-Host ""
Write-Host "Acesso LOCAL (sua maquina):" -ForegroundColor Cyan
Write-Host "   http://localhost:4200" -ForegroundColor White
Write-Host ""
Write-Host "Acesso EXTERNO (outras maquinas na rede):" -ForegroundColor Cyan
Write-Host "   http://${ip}:4200" -ForegroundColor White
Write-Host ""
Write-Host "Aguarde ~60 segundos para todos os servicos iniciarem completamente." -ForegroundColor Yellow
Write-Host ""
Write-Host "Para verificar o status: docker-compose ps" -ForegroundColor Gray