from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import requests
from drf_yasg import openapi
import requests
from drf_yasg.utils import swagger_auto_schema
from rest_framework.permissions import AllowAny
import xmltodict


from .serializers import *
from .hateoas import add_hateoas_links, add_collection_links


def _normalize_arquivo(raw: dict) -> dict:
    """Normaliza um dicionário vindo do serviço SOAP para as chaves esperadas
    pelo `ArquivoResponseSerializer`.
    """
    if not isinstance(raw, dict):
        return raw

    def _extract_text(value):
        # Handle nested structures from xmltodict: dict with '#text', attributes like '@url', lists, etc.
        if value is None:
            return None
        if isinstance(value, list):
            # prefer first non-null extraction
            for v in value:
                t = _extract_text(v)
                if t is not None:
                    return t
            return None
        if isinstance(value, dict):
            # Common xmltodict patterns
            if '#text' in value:
                return value.get('#text')
            if 'text' in value:
                return value.get('text')
            # attributes often start with @
            for k, v in value.items():
                if k.startswith('@') and isinstance(v, str):
                    return v
            # if nested, try to find any field that looks like url or download
            for k, v in value.items():
                if 'url' in k.lower() or 'download' in k.lower() or 'link' in k.lower() or 'href' in k.lower():
                    t = _extract_text(v)
                    if t is not None:
                        return t
            return None
        # primitive
        return value

    def _pick(*keys):
        for k in keys:
            if k in raw:
                return _extract_text(raw[k])
        return None

    arquivo_id = _pick('arquivoId', 'arquivo_id', 'id')
    name = _pick('name', 'nome', 'fileName', 'filename')
    tamanho = _pick('tamanhoArquivo', 'tamanho', 'size')
    possui_foto = _pick('possuiFoto', 'possui_foto', 'possuiFotoArquivo', 'hasPhoto', 'possui')
    usuario_id = _pick('usuarioId', 'usuario_id', 'usuario')
    url_download = _pick('urlDownload', 'url_download', 'url', 'downloadUrl', 'url_download_arquivo')

    # If still None, try to heuristically find any key containing url/download/href
    if url_download is None:
        for k, v in raw.items():
            if 'url' in k.lower() or 'download' in k.lower() or 'href' in k.lower() or 'link' in k.lower():
                url_download = _extract_text(v)
                if url_download is not None:
                    break

    # Conversões simples de tipo
    try:
        if tamanho is not None:
            tamanho = int(tamanho)
    except Exception:
        tamanho = None

    if isinstance(possui_foto, str):
        possui_foto = possui_foto.lower() in ('true', '1', 'yes')
    elif isinstance(possui_foto, (int, float)):
        possui_foto = bool(possui_foto)

    normalized = {
        'arquivo_id': arquivo_id,
        'name': name,
        'tamanhoArquivo': tamanho,
        'possuiFoto': possui_foto,
        'usuario_id': usuario_id,
        'url_download': url_download,
    }

    # Preserve any other keys that might be useful downstream
    for k, v in raw.items():
        if k not in normalized or normalized.get(k) is None:
            # keep original raw keys in case serializer or other logic expects them
            normalized.setdefault(k, v)

    return normalized

# ================== Auth Service ==================

AUTH_URL = 'http://localhost:8081/api/v1'

class LoginView(APIView):
    @swagger_auto_schema(
        request_body=LoginRequestSerializer,
        responses={200: LoginResponseSerializer}
    )
    def post(self, request):
        serializer = LoginRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        response = requests.post(f'{AUTH_URL}/login', json=serializer.validated_data)
        
        if response.status_code == 200:
            data = response.json()
            data = add_hateoas_links(data, request, 'login')
            return Response(data, status=response.status_code)
        
        return Response(response.json(), status=response.status_code)

class CadastroView(APIView):
    @swagger_auto_schema(
        request_body=UsuarioRequestSerializer,
        responses={201: UsuarioResponseSerializer}
    )
    def post(self, request):
        serializer = UsuarioRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        response = requests.post(f'{AUTH_URL}/cadastro', json=serializer.validated_data)
        
        if response.status_code == 201:
            data = response.json()
            data = add_hateoas_links(data, request, 'usuario')
            return Response(data, status=response.status_code)
        
        return Response(response.json(), status=response.status_code)


example_vincular = openapi.Schema(
    type=openapi.TYPE_OBJECT,
    properties={
        'googleAccessToken': openapi.Schema(type=openapi.TYPE_STRING, example="string"),
        'googleRefreshToken': openapi.Schema(type=openapi.TYPE_STRING, example="string"),
        'googleId': openapi.Schema(type=openapi.TYPE_STRING, example="string"),
        'googleName': openapi.Schema(type=openapi.TYPE_STRING, example="string"),
        'googlePictureUrl': openapi.Schema(type=openapi.TYPE_STRING, example="string"),
    }
)

class VincularGoogleView(APIView):
    @swagger_auto_schema(
        request_body=example_vincular,
        responses={200: VincularGoogleResponseSerializer},
        security=[{'Bearer': []}]
    )
    def post(self, request, user_id):
        # Extrai o token JWT do header Authorization
        auth_header = request.headers.get('Authorization', '')
        print(f"Authorization header recebido: '{auth_header[:50]}...'")
        
        if not auth_header:
            print("Token não fornecido")
            return Response(
                {"error": "Token de autenticação não fornecido"},
                status=status.HTTP_401_UNAUTHORIZED
            )
        
        # Aceita tanto "Bearer token" quanto apenas "token"
        if auth_header.startswith('Bearer '):
            token = auth_header.split(' ')[1]
        else:
            token = auth_header
        
        if not token:
            print("Token vazio após processamento")
            return Response(
                {"error": "Token de autenticação inválido"},
                status=status.HTTP_401_UNAUTHORIZED
            )
        
        print(f"Token extraído com sucesso")
        
        serializer = VincularGoogleRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        
        # Envia o token JWT para o backend Spring
        print(f"Enviando request para Spring: {AUTH_URL}/{user_id}/vincular-google")
        response = requests.post(
            f"{AUTH_URL}/{user_id}/vincular-google",
            json=serializer.validated_data,
            headers={'Authorization': f'Bearer {token}'}
        )
        print(f"Response do Spring: status={response.status_code}")
        
        try:
            data = response.json()
            if response.status_code == 200:
                data = add_hateoas_links(data, request, 'vincular-google', user_id)
        except Exception:
            data = response.text or None
        return Response(data, status=response.status_code)

# ================== Arquivo Service ==================

ARQUIVO_URL = 'http://localhost:8082/api/v1'

arquivo_param = openapi.Parameter(
    name='arquivo',
    in_=openapi.IN_FORM,
    type=openapi.TYPE_FILE,
    required=True,
    description='Arquivo a ser convertido',
)

novo_tipo_param = openapi.Parameter(
    name='novoTipo',
    in_=openapi.IN_FORM,
    type=openapi.TYPE_STRING,
    required=True,
    description='Tipo do novo arquivo (PDF, JPG, DOCX, etc.)',
)

class ConverterView(APIView):
    permission_classes = [AllowAny]

    @swagger_auto_schema(
        manual_parameters=[arquivo_param, novo_tipo_param],
        consumes=['multipart/form-data'],
        responses={200: ConversaoResponseSerializer},
        security=[{'Bearer': []}],
    )
    def post(self, request):
        print("HEADERS GATEWAY:", dict(request.headers))
        auth_header = request.headers.get('Authorization', '')
        print("AUTH_HEADER:", auth_header)

        if not auth_header:
            return Response(
                {"error": "Token de autenticação não fornecido"},
                status=status.HTTP_401_UNAUTHORIZED
            )

        # Garante o formato "Bearer <token>"
        if auth_header.startswith("Bearer "):
            forward_auth = auth_header
        else:
            forward_auth = f"Bearer {auth_header}"

        serializer = ConversaoRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        arquivo = serializer.validated_data['arquivo']
        novo_tipo = serializer.validated_data['novoTipo']

        files = {
            'arquivo': (arquivo.name, arquivo.read(), arquivo.content_type),
        }
        data = {
            'novoTipo': novo_tipo
        }

        response = requests.post(
            f'{ARQUIVO_URL}/converter',
            headers={'Authorization': forward_auth},
            files=files,
            data=data
        )

        try:
            data = response.json()
            if response.status_code == 200:
                data = add_hateoas_links(data, request, 'conversao')
        except Exception:
            data = response.text or None

        return Response(data, status=response.status_code)


# ================== SOAP Service ==================

SOAP_URL = 'http://localhost:8083/ws'  # endpoint de POST SOAP


class PerfilView(APIView):
    @swagger_auto_schema(
        responses={200: PerfilcomArquivosSerializer},
        security=[{'Bearer': []}]
    )
    def get(self, request, user_id):
        # 1) Extrai e valida token
        auth_header = request.headers.get('Authorization', '')
        if not auth_header:
            return Response(
                {"error": "Token de autenticação não fornecido"},
                status=status.HTTP_401_UNAUTHORIZED
            )

        if auth_header.startswith("Bearer "):
            forward_auth = auth_header
            token = auth_header.split(" ", 1)[1]  # só o JWT
        else:
            forward_auth = f"Bearer {auth_header}"
            token = auth_header

        # 2) Chama Auth Service para pegar perfil
        try:
            perfil_resp = requests.get(
                f"{AUTH_URL}/{user_id}/perfil",
                headers={'Authorization': forward_auth},
                timeout=5
            )
        except requests.RequestException:
            return Response(
                {"error": "Falha ao comunicar com o Auth Service"},
                status=status.HTTP_502_BAD_GATEWAY
            )

        if perfil_resp.status_code != 200:
            return Response(perfil_resp.json(), status=perfil_resp.status_code)

        usuario_data = perfil_resp.json()
        print("USUARIO_DATA DO AUTH:", usuario_data)

        # 3) Monta envelope SOAP para BuscarArquivoRequest (usuarioId + token)
        envelope = f"""<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:arq="http://fileflip.com/arquivo">
<soapenv:Header/>
<soapenv:Body>
<arq:BuscarArquivoRequest>
<arq:usuarioId>{user_id}</arq:usuarioId>
<arq:token>{token}</arq:token>
</arq:BuscarArquivoRequest>
</soapenv:Body>
</soapenv:Envelope>"""

        print(f"Envelope SOAP enviado:")
        print(envelope)
        print(f"user_id={user_id}, token={token[:30]}...")


        headers = {
            "Content-Type": "text/xml;charset=UTF-8",
        }

        try:
            soap_resp = requests.post(SOAP_URL, data=envelope.encode("utf-8"), headers=headers, timeout=5)
        except requests.RequestException:
            return Response(
                {"error": "Falha ao comunicar com o serviço SOAP de arquivos"},
                status=status.HTTP_502_BAD_GATEWAY
            )

        if soap_resp.status_code != 200:
            return Response(
                {"error": "Erro no serviço SOAP de arquivos", "status": soap_resp.status_code},
                status=status.HTTP_502_BAD_GATEWAY
            )

        # 4) Converte XML SOAP -> dict -> lista de arquivos
        xml_dict = xmltodict.parse(soap_resp.content)

        body = xml_dict.get("soapenv:Envelope", {}).get("soapenv:Body") \
               or xml_dict.get("SOAP-ENV:Envelope", {}).get("SOAP-ENV:Body") \
               or list(xml_dict.values())[0].get("Body")

        buscar_resp = None
        if body:
            for key, value in body.items():
                if "BuscarArquivoResponse" in key:
                    buscar_resp = value
                    break

        arquivos_raw = []
        if buscar_resp:
            arquivos_raw = buscar_resp.get("arquivos", [])
            if isinstance(arquivos_raw, dict):
                arquivos_raw = [arquivos_raw]

        # DEBUG: imprime arquivos antes da normalização
        print("===== ARQUIVOS_RAW ANTES DA NORMALIZAÇÃO =====")
        for idx, arq in enumerate(arquivos_raw):
            print(f"Arquivo {idx}:")
            print(f"  Tipo: {type(arq)}")
            print(f"  Chaves: {list(arq.keys()) if isinstance(arq, dict) else 'N/A'}")
            print(f"  Conteúdo completo: {arq}")
        print("=" * 50)

        # Normaliza cada arquivo para o formato esperado pelo serializer
        arquivos_raw = [_normalize_arquivo(a) for a in arquivos_raw]

        # DEBUG: imprime arquivos depois da normalização
        print("===== ARQUIVOS_RAW DEPOIS DA NORMALIZAÇÃO =====")
        for idx, arq in enumerate(arquivos_raw):
            print(f"Arquivo {idx}:")
            print(f"  url_download: {arq.get('url_download')}")
            print(f"  Todas as chaves: {list(arq.keys())}")
        print("=" * 50)

        # 5) Monta payload combinado
        payload = {
            "usuario": usuario_data,
            "arquivos": arquivos_raw,
        }

        # 6) Aplica serializer
        print("ARQUIVOS_RAW:", arquivos_raw)
        serializer = PerfilcomArquivosSerializer(payload)
        data = serializer.data

        # 7) HATEOAS
        data = add_hateoas_links(data, request, 'perfil', user_id)
        return Response(data, status=status.HTTP_200_OK)
