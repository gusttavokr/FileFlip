from django.urls import path
from .views import (
    LoginView, CadastroView, VincularGoogleView,
    ConverterView,
)

urlpatterns = [
    path("auth/login", LoginView.as_view(), name="login"),
    path("auth/usuarios/cadastro", CadastroView.as_view(), name="cadastrar-usuario"),
    path("auth/usuarios/<uuid:user_id>/vincular-google", VincularGoogleView.as_view(), name="vincular-google"),
    path("api/v1/arquivos/converter/", ConverterView.as_view(), name="converter-arquivo"),
]
