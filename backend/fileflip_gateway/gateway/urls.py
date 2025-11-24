from django.urls import path
from .views import (
    LoginView,
    CadastroView,
    VincularGoogleView,
)

urlpatterns = [
    # auth_service
    path("auth/login", LoginView.as_view(), name="login"),
    path("auth/usuarios/cadastro", CadastroView.as_view(), name="cadastrar-usuario"),
    path("auth/usuarios/<uuid:user_id>/vincular-google", VincularGoogleView.as_view(), name="vincular-google"),
]
