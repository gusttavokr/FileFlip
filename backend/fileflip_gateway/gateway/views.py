from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
import requests

from drf_yasg import openapi
import requests


from drf_yasg.utils import swagger_auto_schema

from .serializers import (
    LoginRequestSerializer, LoginResponseSerializer,
    UsuarioRequestSerializer, UsuarioResponseSerializer,
    VincularGoogleRequestSerializer, VincularGoogleResponseSerializer
)

SPRING_BASE_URL = 'http://localhost:8081/api/v1/usuarios'

class LoginView(APIView):
    @swagger_auto_schema(
        request_body=LoginRequestSerializer,
        responses={200: LoginResponseSerializer}
    )
    def post(self, request):
        serializer = LoginRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        response = requests.post(f'{SPRING_BASE_URL}/login', json=serializer.validated_data)
        return Response(response.json(), status=response.status_code)

class CadastroView(APIView):
    @swagger_auto_schema(
        request_body=UsuarioRequestSerializer,
        responses={201: UsuarioResponseSerializer}
    )
    def post(self, request):
        serializer = UsuarioRequestSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        response = requests.post(f'{SPRING_BASE_URL}/cadastro', json=serializer.validated_data)
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
        print(f"Enviando request para Spring: {SPRING_BASE_URL}/{user_id}/vincular-google")
        response = requests.post(
            f"{SPRING_BASE_URL}/{user_id}/vincular-google",
            json=serializer.validated_data,
            headers={'Authorization': f'Bearer {token}'}
        )
        print(f"Response do Spring: status={response.status_code}")
        
        try:
            data = response.json()
        except Exception:
            data = response.text or None
        return Response(data, status=response.status_code)