from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

from .clients import ClientsConfig
from .soap_client import SoapClient
from .serializers import GenericBodySerializer

clients = ClientsConfig()


# ========== arquivo_service ==========

class ListarArquivosView(APIView):
    def get(self, request):
        usuario_id = request.query_params.get("usuarioId")

        if not usuario_id:
            return Response({"erro": "usuarioId obrigatório"}, status=400)

        resp = clients.arquivo_client.get(
            "/api/arquivos",
            params={"usuarioId": usuario_id}
        )
        return Response(resp.json())


class ConverterArquivoView(APIView):
    def post(self, request):
        if "arquivo" not in request.FILES:
            return Response({"erro": "arquivo obrigatório"}, status=400)

        files = {"arquivo": request.FILES["arquivo"]}
        data = {"formatoDestino": request.data.get("formatoDestino")}

        resp = clients.arquivo_client.post(
            "/converter",
            files=files,
            data=data
        )
        return Response(resp.json())
    

# ========== auth_service ==========

class LoginView(APIView):
    def post(self, request):
        serializer = GenericBodySerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        resp = clients.auth_client.post(
            "/api/v1/usuarios/login",
            json=serializer.validated_data
        )
        return Response(resp.json())


class CadastrarUsuarioView(APIView):
    def post(self, request):
        serializer = GenericBodySerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        resp = clients.auth_client.post(
            "/api/v1/usuarios",
            json=serializer.validated_data
        )
        return Response(resp.json(), status=status.HTTP_201_CREATED)


class VincularGoogleView(APIView):
    def post(self, request, id):
        serializer = GenericBodySerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        resp = clients.auth_client.post(
            f"/api/v1/usuarios/{id}/vincular-google",
            json=serializer.validated_data
        )
        return Response(resp.json())


class AtualizarUsuarioView(APIView):
    def put(self, request, id):
        serializer = GenericBodySerializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        resp = clients.auth_client.put(
            f"/api/v1/usuarios/{id}",
            json=serializer.validated_data
        )
        return Response(resp.json())


class ListarUsuariosView(APIView):
    def get(self, request):
        resp = clients.auth_client.get("/api/v1/usuarios")
        return Response(resp.json())


class DeletarUsuarioView(APIView):
    def delete(self, request, id):
        resp = clients.auth_client.delete(f"/api/v1/usuarios/{id}")
        return Response(status=resp.status_code)


# ========== SOAP ==========

class ListarArquivosSoapView(APIView):
    def get(self, request):
        usuario_id = request.query_params.get("usuarioId")

        if not usuario_id:
            return Response({"erro": "usuarioId obrigatório"}, status=400)

        soap = SoapClient()
        result = soap.listar_arquivos(usuario_id)

        return Response(result)
