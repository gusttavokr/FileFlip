from django.urls import path
from .views import (
    ListarArquivosView,
    ConverterArquivoView,
    LoginView,
    CadastrarUsuarioView,
    VincularGoogleView,
    AtualizarUsuarioView,
    ListarUsuariosView,
    DeletarUsuarioView,
    ListarArquivosSoapView
)

urlpatterns = [
    # arquivo_service
    path("arquivos", ListarArquivosView.as_view()),
    path("arquivos/converter", ConverterArquivoView.as_view()),

    # auth_service
    path("auth/login", LoginView.as_view()),
    path("auth/usuarios/cadastro", CadastrarUsuarioView.as_view()),
    path("auth/usuarios/listar", ListarUsuariosView.as_view()),
    path("auth/usuarios/<uuid:id>", AtualizarUsuarioView.as_view()),
    path("auth/usuarios/<uuid:id>/delete", DeletarUsuarioView.as_view()),
    path("auth/usuarios/<uuid:id>/vincular-google", VincularGoogleView.as_view()),

    # soap
    path("soap/arquivos", ListarArquivosSoapView.as_view()),
]
