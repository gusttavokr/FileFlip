from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import requests
from drf_yasg import openapi
import requests
from drf_yasg.utils import swagger_auto_schema
from rest_framework.permissions import AllowAny

from .serializers import *

# ================== Auth Service ==================

AUTH_URL = 'http://localhost:8081/api/v1/usuarios'

class LoginView(APIView):
    @swagger_auto_schema(
        request_body=LoginRequestSerializer,
        responses={200: LoginResponseSerializer}
    )
    def post(self, request):
        serializer = LoginRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        response = requests.post(f'{AUTH_URL}/login', json=serializer.validated_data)
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
        except Exception:
            data = response.text or None
        return Response(data, status=response.status_code)
    
# ================== Arquivo Service ==================

ARQUIVO_URL = 'http://localhost:8082/api/v1/arquivo'

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
        except Exception:
            data = response.text or None

        return Response(data, status=response.status_code)
